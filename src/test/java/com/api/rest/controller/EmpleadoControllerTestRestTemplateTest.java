package com.api.rest.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.api.rest.model.Empleado;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerTestRestTemplateTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    void testGuardarEmpleado(){
            Empleado empleado = Empleado.builder()
                    .id(1L)
                    .nombre("Christian")
                    .apellido("Ramirez")
                    .email("c1@gmail.com")
                    .build();
            ResponseEntity<Empleado> respuesta = testRestTemplate.postForEntity("http://localhost:8080/api/empleados",empleado,Empleado.class);
            assertEquals(HttpStatus.CREATED,respuesta.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());

            Empleado empleadoCreado = respuesta.getBody();
            assertNotNull(empleadoCreado);

            assertEquals(1L,empleadoCreado.getId());
            assertEquals("Christian",empleadoCreado.getNombre());
            assertEquals("Ramirez",empleadoCreado.getApellido());
            assertEquals("c1@gmail.com",empleadoCreado.getEmail());
    }

    @Test
    @Order(2)
    void testListarEmpleados(){

        ResponseEntity<Empleado[]> response = testRestTemplate.getForEntity("http://localhost:8080/api/empleados", Empleado[].class);

        List<Empleado> empleados = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());

        assertEquals(1, empleados.size());
        assertEquals(1L, empleados.get(0).getId());
        assertEquals("Christian", empleados.get(0).getNombre());

    }

    @Test
    @Order(3)
    void testObtenerUnEmpleado(){

        ResponseEntity<Empleado> respuesta = testRestTemplate.getForEntity("http://localhost:8080/api/empleados/1",Empleado.class);
        assertEquals(HttpStatus.OK,respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());

        Empleado empleadoCreado = respuesta.getBody();
        assertNotNull(empleadoCreado);

        assertEquals(1L,empleadoCreado.getId());
        assertEquals("Christian",empleadoCreado.getNombre());
    }

    @Test
    @Order(4)
    void testEliminarUnEmpleado(){
        ResponseEntity<Empleado[]> response = testRestTemplate.getForEntity("http://localhost:8080/api/empleados", Empleado[].class);

        List<Empleado> empleados = Arrays.asList(response.getBody());

        assertEquals(1, empleados.size());

        Map<String,Long> pathVariables = new HashMap<>();
        pathVariables.put("id",1L);

        ResponseEntity<Void> exchange = testRestTemplate.exchange("http://localhost:8080/api/empleados/{id}", HttpMethod.DELETE,null,Void.class,pathVariables);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        response = testRestTemplate.getForEntity("http://localhost:8080/api/empleados", Empleado[].class);
        empleados = Arrays.asList(response.getBody());
        assertEquals(0,empleados.size());

        ResponseEntity<Empleado> respuestaDetalle = testRestTemplate.getForEntity("http://localhost:8080/api/empleados/2", Empleado.class);

        assertEquals(HttpStatus.NOT_FOUND, respuestaDetalle.getStatusCode());
        assertFalse(respuestaDetalle.hasBody());

    }
}
