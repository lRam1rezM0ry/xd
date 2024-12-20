/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.platosparte.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

/**
 *
 * @author Jeanc
 */
@Entity
public class Plato {

    @Id
    @NotNull(message = "El ID del plato no puede ser nulo")
    private String idPlato;

    private String nombre;

    private String descripcion;

    private double precio;

    @Lob
    private byte[] imagen;

    @Transient
    private MultipartFile archivoImagen; // Para manejar la carga del archivo

    // Getters y Setters
    public String getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(String idPlato) {
        this.idPlato = idPlato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public MultipartFile getArchivoImagen() {
        return archivoImagen;
    }

    public void setArchivoImagen(MultipartFile archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

    // Método para convertir la imagen a Base64
    public String getImagenBase64() {
        if (imagen != null) {
            return Base64.getEncoder().encodeToString(imagen);
        }
        return null;
    }
}