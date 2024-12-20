/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.platosparte.service;

import com.example.platosparte.model.Plato;
import com.example.platosparte.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jeanc
 */
@Service
public class PlatoService {

    @Autowired
    private PlatoRepository repository;

    public List<Plato> listarTodos() {
        return repository.findAll();
    }

    public void guardar(Plato plato) {
        repository.save(plato);
    }

    public Optional<Plato> buscarPorId(String id) {
        return repository.findById(id);
    }

    public void eliminar(String id) {
        repository.deleteById(id);
    }
}