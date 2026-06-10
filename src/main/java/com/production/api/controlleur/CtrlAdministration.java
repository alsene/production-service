package com.production.api.controlleur;

import com.production.api.model.Produit;
import com.production.api.model.ResponseProduction;
import com.production.api.service.FacadeProductionService;
import com.production.api.service.SrvProduit;
import com.production.api.util.Retour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/production/endpoint/administration/v1")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CtrlAdministration {
    private final SrvProduit srvProduit;
    private final FacadeProductionService facadeProductionService;

}
