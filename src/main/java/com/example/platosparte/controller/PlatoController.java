/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.platosparte.controller;

import com.example.platosparte.model.Plato;
import com.example.platosparte.service.PlatoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

// importaciones pdf
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

// importaciones excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Jeanc
 */
@Controller
@RequestMapping("/platos")
public class PlatoController {

    private final PlatoService service;

    public PlatoController(PlatoService service) {
        this.service = service;
    }

    @GetMapping
    public String listarPlatos(Model model) {
        model.addAttribute("platos", service.listarTodos());
        return "platos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("plato", new Plato());
        return "formularioPlato";
    }

    @PostMapping
    public String guardarPlato(@ModelAttribute Plato plato) {
        try {
            if (plato.getIdPlato() != null && service.buscarPorId(plato.getIdPlato()).isPresent()) {
                // Es una actualización
                Plato platoExistente = service.buscarPorId(plato.getIdPlato()).get();

                // Actualizar solo los campos modificables
                platoExistente.setNombre(plato.getNombre());
                platoExistente.setDescripcion(plato.getDescripcion());
                platoExistente.setPrecio(plato.getPrecio());

                // Actualizar imagen solo si se subió una nueva
                if (plato.getArchivoImagen() != null && !plato.getArchivoImagen().isEmpty()) {
                    platoExistente.setImagen(plato.getArchivoImagen().getBytes());
                }

                service.guardar(platoExistente);
            } else {
                // Es un nuevo registro
                String nuevoId = "CRT" + String.format("%03d", service.listarTodos().size() + 1);
                plato.setIdPlato(nuevoId);

                if (plato.getArchivoImagen() != null && !plato.getArchivoImagen().isEmpty()) {
                    plato.setImagen(plato.getArchivoImagen().getBytes());
                }

                service.guardar(plato);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/platos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) {
        Plato plato = service.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("plato", plato);
        return "formularioPlato";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPlato(@PathVariable String id) {
        service.eliminar(id);
        return "redirect:/platos";
    }

    @GetMapping("/reporte/pdf")
    public void generarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=platos_reporte.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Reporte de Platos").setBold().setFontSize(18));

        Table table = new Table(4);
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Descripción");
        table.addCell("Precio");

        List<Plato> platos = service.listarTodos();
        for (Plato plato : platos) {
            table.addCell(plato.getIdPlato());
            table.addCell(plato.getNombre());
            table.addCell(plato.getDescripcion());
            table.addCell(String.valueOf(plato.getPrecio()));
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/reporte/excel")
    public void generarExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=platos_reporte.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Platos");

        Row headerRow = sheet.createRow(0);
        String[] columnHeaders = {"ID", "Nombre", "Descripción", "Precio"};
        for (int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        List<Plato> platos = service.listarTodos();
        int rowIndex = 1;
        for (Plato plato : platos) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(plato.getIdPlato());
            row.createCell(1).setCellValue(plato.getNombre());
            row.createCell(2).setCellValue(plato.getDescripcion());
            row.createCell(3).setCellValue(plato.getPrecio());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
