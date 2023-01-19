package com.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.api.rest.model.Empleado;
import com.api.rest.service.EmpleadoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGuardarEmpleado() throws Exception {

        Empleado empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

        given(empleadoService.saveEmpleado(any(empleado.getClass())))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));

    }

    @Test
    void testListarEmpleados() throws Exception {

        //given
        List<Empleado> listaEmpleados = new ArrayList<>();

        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Ramirez").email("g1@gmail.com").build());

        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);

        //when

        ResultActions response = mockMvc.perform(get("/api/empleados"));

        //then

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listaEmpleados.size())));

    }

    @Test
    void obtenerEmpleadoPorId() throws Exception{
        //given
        Long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleado));

        //when
        ResultActions responese = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        responese.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));


    }

    @Test
    void obtenerEmpleadoNoEncontrado() throws Exception{
        //given
        Long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        //when
        ResultActions responese = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        responese.andExpect(status().isNotFound());

    }

    @Test
    void testActualizarEmpleado() throws Exception {
        //given
        Long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Christian Raul")
                .apellido("Ramirez")
                .email("c231@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleadoGuardado));

        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when

        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre", is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleadoActualizado.getEmail())));
    }

    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception {
        //given
        Long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Christian Raul")
                .apellido("Ramirez")
                .email("c231@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when

        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testEliminarEmpleado() throws Exception {
        Long empleadoId = 1L;

        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);

        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}",empleadoId));

        response.andExpect(status().isOk())
                .andDo(print());

    }



}
