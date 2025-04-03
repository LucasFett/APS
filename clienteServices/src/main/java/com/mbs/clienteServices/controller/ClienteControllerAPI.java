package com.mbs.clienteServices.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mbs.clienteServices.entidades.Cliente;

@Controller
@CrossOrigin(origins = "http://localhost:9005")
@RequestMapping("/v1/cliente")
public class ClienteControllerAPI {

    private List<Cliente> listaCliente = new ArrayList<>();
    private static Integer id = 0;

    @PostMapping
    public ResponseEntity<String> salvar(@RequestBody Cliente cliente) {
        System.out.println("Executando salvar " + cliente);

        if (cliente.getNome() == null || cliente.getNome().length() <= 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do cliente deve ter no mínimo 3 caracteres");
        }
        cliente.setId(++id);
        listaCliente.add(cliente);
        return ResponseEntity.ok(id.toString());
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        System.out.println("Executando listar");
        return ResponseEntity.ok(listaCliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable Integer id) {
        System.out.println("Executando buscarCliente para ID: " + id);

        Optional<Cliente> clienteEncontrado = listaCliente.stream()
                .filter(cliente -> cliente.getId().equals(id))
                .findFirst();

        return clienteEncontrado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/existe/{cpf}")
    public ResponseEntity<Boolean> existeCliente(@PathVariable String cpf) {
        System.out.println("Executando existeCliente para CPF: " + cpf);

        boolean existe = listaCliente.stream()
                .anyMatch(cliente -> cliente.getCpf().equals(cpf));

        return ResponseEntity.ok(existe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteAtualizado) {
        System.out.println("Executando atualizarCliente para ID: " + id);

        for (Cliente cliente : listaCliente) {
            if (cliente.getId().equals(id)) {
                cliente.setNome(clienteAtualizado.getNome());
                cliente.setEmail(clienteAtualizado.getEmail());
                cliente.setCpf(clienteAtualizado.getCpf());
                cliente.setCep(clienteAtualizado.getCep());
                return ResponseEntity.ok("Cliente atualizado com sucesso!");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        System.out.println("Executando deletar cliente ID: " + id);

        boolean resultado = listaCliente.removeIf(cliente -> cliente.getId().equals(id));

        if (resultado) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
