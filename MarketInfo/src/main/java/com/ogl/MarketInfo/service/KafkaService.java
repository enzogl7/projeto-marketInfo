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

    @Autowired
    private MailService mailService;

    @KafkaListener(topics = "estoque-alert", groupId = "market-info")
    public void processaEstoque(String mensagem) {
        OpcoesMensageria op = opcoesMensageriaService.findAll().stream().findFirst().orElse(new OpcoesMensageria());
        if (op != null) {
            for (String email : op.getEmails()) {
                mailService.enviarEmailNotificacao(email, "Notificação de estoque | MarketInfo - Gestão de mercados", mensagem, "emails/notificacao_estoque");
            }
        }
    }

    @KafkaListener(topics = "produtos-alert", groupId = "market-info")
    public void processaProdutos(String mensagem) {
        OpcoesMensageria op = opcoesMensageriaService.findAll().stream().findFirst().orElse(new OpcoesMensageria());
        if (op != null) {
            for (String email : op.getEmails()) {
                mailService.enviarEmailNotificacao(email, "Notificação de estoque | MarketInfo - Gestão de mercados", mensagem, "emails/notificacao_produtos");
            }
        }
        System.out.println("Mensagem recebida: " + mensagem);
    }

}
