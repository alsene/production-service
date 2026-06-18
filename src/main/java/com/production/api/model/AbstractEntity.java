/**
 * E-PAIEMENT : Version 1.0.0, 07 Mai 2024
 *
 *   ____  _____  ______ _   _      _____ _______
 *  / __ \|  __ \|  ____| \ | |    |_   _|__   __|
 * | |  | | |__) | |__  |  \| |______| |    | |
 * | |  | |  ___/|  __| | . ` |______| |    | |
 * | |__| | |    | |____| |\  |     _| |_   | |
 *  \____/|_|    |______|_| \_|    |_____|  |_|
 *
 *
 * Ce code source est la propriete de OPEN IT.
 * Il a ete livre dans le cadre du projet e-paiement.
 *
 * Ce code ne doit en aucun cas etre modifie et ou altere sans en avoir
 * communiquer a OPEN IT a l'adresse : devs@open-it.sn.
 *
 * Tous les droits sont reserves a OPEN IT : https://www.open-it.sn
 */
package com.production.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable {

	@Serial
    private static final long serialVersionUID = 266656365874334873L;

	@CreatedDate
	@Column(name = "DATE_CREATION", updatable = false, length = 19)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonIgnore
	private Date DateCreation = new Date();

	@CreatedBy
	@Column(name = "USER_CREATION", updatable = false, length = 80)
	@JsonIgnore
	private Long idUserCreation;

	@LastModifiedDate
	@Column(name = "DATE_MODIFICATION", length = 19)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonIgnore
	private Date DateModification = new Date();

	@LastModifiedBy
	@Column(name = "USER_MODIFICATION", length = 80)
	@JsonIgnore
	private Long idUserModification;

    @Column(name = "is_supprimer", columnDefinition = "boolean default false", nullable = false)
    private boolean supprimer;
}
