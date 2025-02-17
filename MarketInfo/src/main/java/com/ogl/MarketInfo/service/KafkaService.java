package com.ogl.MarketInfo.service;

import com.ogl.MarketInfo.model.Estoque;
import com.ogl.MarketInfo.model.OpcoesMensageria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private OpcoesMensageriaService opcoesMensageriaService;

    @KafkaListener(topics = "estoque-alert", groupId = "market-info")
    public void processaEstoque(String mensagem) {
        // adicionar logica de envio de email para a lista especificada na opcoesmensageria
        System.out.println("Mensagem recebida: " + mensagem);
    }

}
