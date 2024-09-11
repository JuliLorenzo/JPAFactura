package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("en marcha..");

        try {
            // Iniciar una Transaccion
            entityManager.getTransaction().begin();

            //Crear 2 Categorias
            Categoria deporte = Categoria.builder().denominacion("Deportivo").build();
            Categoria urbano = Categoria.builder().denominacion("Urbano").build();

            //Crear 2 Articulos
            Articulo zapatillaAdidas = Articulo.builder()
                    .cantidad(10)
                    .precio(150000)
                    .build();

            Articulo remeraNike = Articulo.builder()
                    .cantidad(5)
                    .precio(30000)
                    .build();

            //Seteo Categoria al Articulo y el Articulo a la Categoria
            zapatillaAdidas.getCategorias().add(deporte);
            deporte.getArticulos().add(zapatillaAdidas);

            zapatillaAdidas.getCategorias().add(urbano);
            urbano.getArticulos().add(zapatillaAdidas);

            remeraNike.getCategorias().add(urbano);
            urbano.getArticulos().add(remeraNike);

            //Crear una Factura
            Factura factura1 = Factura.builder()
                    .numero(405)
                    .fecha("07/09/2024")
                    .build();

            // Crear un Domicilio
            Domicilio nuevoDomicilio = Domicilio.builder()
                    .nombreCalle("Renato Zanzin")
                    .numero(2151)
                    .build();

            // Crear un nuevo Cliente
            Cliente cliente1 = Cliente.builder()
                    .nombre("Julieta")
                    .dni(44675079)
                    .build();

            cliente1.setDomicilio(nuevoDomicilio);

            // Seteo el Cliente a la Factura
            factura1.setCliente(cliente1);


            //Crear 2 Detalle Factura
            DetalleFactura detalle1 = DetalleFactura.builder()
                    .articulo(remeraNike)
                    .cantidad(2)
                    .subtotal(60000)
                    .build();

            DetalleFactura detalle2 = new DetalleFactura();

            detalle2.setArticulo(zapatillaAdidas);
            detalle2.setCantidad(1);
            detalle2.setSubtotal(150000);

            factura1.getFacturas().add(detalle1);
            factura1.getFacturas().add(detalle2);

            factura1.setTotal(210000);

            //Grabamos la Factura en la Base de Datos
            entityManager.persist(factura1);

            //Limpia la conexion
            entityManager.flush();

            //Commit del persist
            entityManager.getTransaction().commit();

            // Consultar y mostrar la entidad persistida
            Factura retrievedFactura = entityManager.find(Factura.class,factura1.getIdFactura());
            System.out.println("El total de la Factura " + retrievedFactura.getNumero() + " es de $" + retrievedFactura.getTotal());
        }

        catch (Exception e){

            //Si ocurre un error, hacemos un rollback
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
            System.out.println("Error");}

        // Cerrar la conexion con el EntityManager y el EntityManagerFactory
        entityManager.close();
        entityManagerFactory.close();

    }
}
