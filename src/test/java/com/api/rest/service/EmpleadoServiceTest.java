package com.api.rest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.api.rest.exception.ResourceNotFoundException;
import com.api.rest.model.Empleado;
import com.api.rest.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setUp(){
        empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
    }

    @Test
    void testGuardarEmpleado(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());

        given(empleadoRepository.save(empleado)).willReturn(empleado);

        //when
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);

        //then
        assertThat(empleadoGuardado).isNotNull();

    }

    @Test
    void testGuardarEmpleadoConThrowException(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));

        //when
        assertThrows(ResourceNotFoundException.class,() ->{
            empleadoService.saveEmpleado(empleado);
        });

        //then
        verify(empleadoRepository,never()).save(any(empleado.getClass()));
    }

    @Test
    void testListarEmpleados(){
        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Julian")
                .apellido("Gomez")
                .email("j2@gmail.com")
                .build();

        given(empleadoRepository.findAll()).willReturn(List.of(empleado,empleado1));

        //when
        List<Empleado> empleados = empleadoService.getAllEmpleados();

        //then

        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);

    }

   @Test
   void testRetornarListaVacia(){
       Empleado empleado1 = Empleado.builder()
               .nombre("Julian")
               .apellido("Gomez")
               .email("j2@gmail.com")
               .build();

       given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

       List<Empleado> empleados = empleadoService.getAllEmpleados();

       assertThat(empleados).isEmpty();
       assertThat(empleados.size()).isEqualTo(0);

   }

   @Test
   void testObtenerEmpleadoPorId(){
        //given
        given(empleadoRepository.findById(empleado.getId())).willReturn(Optional.of(empleado));

        //when
       Empleado empleadoGuardado = empleadoService.getEmpleadoById(empleado.getId()).get();

       //then
       assertThat(empleadoGuardado).isNotNull();

   }

   @Test
   void testActualizarEmpleado(){

        //given
       given(empleadoRepository.save(empleado)).willReturn(empleado);
       empleado.setEmail("chr2@gmail.com");
       empleado.setNombre("Christian Raul");

       //when
       Empleado empleadoActualizado = empleadoService.updateEmpleado(empleado);

       //then
       assertThat(empleadoActualizado.getEmail()).isEqualTo("chr2@gmail.com");
       assertThat(empleadoActualizado.getNombre()).isEqualTo("Christian Raul");

   }


    @Test
    void testEliminarEmpleado(){

    willDoNothing().given(empleadoRepository).deleteById(empleado.getId());

    //when
    empleadoService.deleteEmpleado(empleado.getId());

    verify(empleadoRepository,times(1)).deleteById(empleado.getId());

    }


}
