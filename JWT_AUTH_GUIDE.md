# JWT Authentication Guide

Cette guide explique comment utiliser les endpoints d'authentification JWT de la production-service.

## Configuration JWT

### Fichier `application.yml`

```yaml
security:
  jwt:
    # Clé secrète base64 pour signer les tokens (32+ octets)
    secret: bXlfc3VwZXJfc2VjcmV0X2tleV9mb3JfcHJvZHVjdGlvbl9zZXJ2aWNlXzIwMjY=
    # Expiration du JWT (24 heures en ms)
    expiration-ms: 86400000
    # Expiration du refresh token (7 jours en ms)
    refresh-expiration-ms: 604800000
```

> **Important** : En production, remplacez la clé secrète par une vraie clé sécurisée (32+ octets en base64).

## Endpoints

### 1. POST `/api/auth/login` - Authentification

**Description** : Authentifie un utilisateur et retourne un JWT + refresh token.

**Request Body** :
```json
{
  "email": "utilisateur@example.com",
  "password": "motdepasse"
}
```

**Validation** :
- `email` : requis, non vide
- `password` : requis, non vide

**Réponse Succès (200)** :
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "username": "utilisateur@example.com",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400
}
```

**Réponse Erreur (400 - Bad Request)** :
```json
{
  "status": 400,
  "message": "Email est requis",
  "error": "BAD_REQUEST",
  "timestamp": 1719874123456
}
```

**Réponse Erreur (401 - Unauthorized)** :
```json
{
  "status": 401,
  "message": "Email ou mot de passe incorrect",
  "error": "UNAUTHORIZED",
  "timestamp": 1719874123456
}
```

### 2. POST `/api/auth/refresh-token` - Renouveler le JWT

**Description** : Utilise un refresh token pour obtenir un nouveau JWT.

**Request Body** :
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Réponse Succès (200)** :
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "username": "utilisateur@example.com",
  "expiresIn": 86400
}
```

**Réponse Erreur (401 - Unauthorized)** :
```json
{
  "status": 401,
  "message": "Refresh token expiré ou invalide",
  "error": "UNAUTHORIZED",
  "timestamp": 1719874123456
}
```

## Utilisation du JWT

### Header Authorization

Après authentification, incluez le token dans chaque requête protégée :

```
Authorization: Bearer <token>
```

**Exemple avec curl** :
```bash
curl -X GET http://localhost:8081/api/production/endpoint/administration/v1/afficherUtilisateurs \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## Rôles et Permissions

Les rôles sont chargés depuis la table `profil_utilisateur` et mappés en authorities Spring Security :

```
ROLE_SUPER_ADMINISTRATEUR
ROLE_ADMINISTRATEUR
ROLE_GESTIONNAIRE
ROLE_SUPERVISEUR
ROLE_OPERATEUR
ROLE_ASSURANCE_QUALITE
```

Si un utilisateur n'a pas de rôle assigné, une autorité `ROLE_USER` par défaut est attribuée.

## Tests avec PowerShell

### Login
```powershell
$loginResponse = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8081/api/auth/login" `
  -ContentType "application/json" `
  -Body @{
    email = "admin@demo.com"
    password = "motdepasse"
  } | ConvertTo-Json

$token = $loginResponse.token
$refreshToken = $loginResponse.refreshToken

Write-Output "Token: $token"
Write-Output "Refresh Token: $refreshToken"
```

### Appeler un endpoint protégé
```powershell
$headers = @{
  Authorization = "Bearer $token"
}

$response = Invoke-RestMethod -Method Get `
  -Uri "http://localhost:8081/api/production/endpoint/administration/v1/afficherUtilisateurs" `
  -Headers $headers

Write-Output $response
```

### Refresh Token
```powershell
$refreshResponse = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8081/api/auth/refresh-token" `
  -ContentType "application/json" `
  -Body @{
    refreshToken = $refreshToken
  } | ConvertTo-Json

$newToken = $refreshResponse.token
Write-Output "Nouveau Token: $newToken"
```

## Sécurité

### Points importants

1. **Stockage du secret JWT** : Ne jamais commiter la clé secrète réelle dans le code source.
   - Utiliser des variables d'environnement ou vaults de secrets (Vault, AWS Secrets Manager, etc.)

2. **HTTPS** : Toujours utiliser HTTPS en production pour transmettre les tokens.

3. **Expiration** : 
   - JWT : 24 heures (réduire si nécessaire)
   - Refresh Token : 7 jours (pour permettre les sessions longues)

4. **Révocation** : Une liste noire de tokens révoqués n'est pas implémentée actuellement.
   - À implémenter pour le logout vrai.

## Security Filter Chain

- Endpoints `/api/auth/**` : **Publics**
- Tous les autres endpoints : **Authentifiés et autorisés**

### Configuration CSRF et CORS

- **CSRF** : Désactivé pour les appels API (utilisation stateless JWT)
- **CORS** : Configuré dans `WebConfig` (optionnel adapter pour votre frontend)

## Troubleshooting

### Token invalide / expiré
```json
{
  "status": 401,
  "message": "Refresh token expiré ou invalide",
  "error": "UNAUTHORIZED"
}
```
→ Authentifiez-vous à nouveau avec `/api/auth/login`

### Utilisateur introuvable
```json
{
  "status": 401,
  "message": "Utilisateur introuvable",
  "error": "UNAUTHORIZED"
}
```
→ Vérifiez que l'utilisateur existe dans la base de données

### Email/Password incorrect
```json
{
  "status": 401,
  "message": "Email ou mot de passe incorrect",
  "error": "UNAUTHORIZED"
}
```
→ Vérifiez vos identifiants

