package com.api.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.api.rest.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

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
        Empleado empleado1 = Empleado.builder()
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@gmail.com")
                .build();
        //when
        Empleado empleadoGuardado = empleadoRepository.save(empleado1);

        //then
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);

    }

    @Test
    void testListarEmpleados(){
        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Julian")
                .apellido("Gomez")
                .email("j2@gmail.com")
                .build();

        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        //when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);
    }

    @Test
    void testObtenerEmpleadoPorId(){

        empleadoRepository.save(empleado);

        //when

        Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();

        assertThat(empleadoBD).isNotNull();

    }

    @Test
    void testActualizarEmpleado(){
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();

        empleadoBD.setEmail("c232@gmail.com");
        empleadoBD.setNombre("Chris");
        empleadoBD.setApellido("Rodriguez");

        Empleado empleadoActualizado = empleadoRepository.save(empleadoBD);

        //then

        assertThat(empleadoBD.getEmail()).isEqualTo("c232@gmail.com");
        assertThat(empleadoBD.getNombre()).isEqualTo("Chris");
        assertThat(empleadoBD.getApellido()).isEqualTo("Rodriguez");

    }

    @Test
    void testEliminarEmpleado(){
        empleadoRepository.save(empleado);

        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());

        assertThat(empleadoOptional).isEmpty();
    }
}
