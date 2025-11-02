package sebas.lab.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas unitarias para GestionBiblioteca
 * 
 * Cada método con @Test representa una prueba individual
 */
public class TestGestionBiblioteca {
    
    // Variable de instancia que usaremos en todos los tests
    private GestionBiblioteca gestion;
    
    /**
     * @Before se ejecuta ANTES de cada test
     * Aquí creamos una nueva instancia de GestionBiblioteca
     * para que cada test empiece con un objeto "limpio"
     */
    @Before
    public void setUp() {
        gestion = new GestionBiblioteca();
    }
    
    // ========================================
    // TESTS PARA: calcularPrecioConDescuento()
    // ========================================
    
    /**
     * TEST 1: Verificar precio sin descuento (0%)
     * 
     * ¿Qué estamos probando?
     * - Si el descuento es 0%, el precio debe quedar igual
     * 
     * Pasos:
     * 1. ARRANGE: Definimos precio base = 100
     * 2. ACT: Calculamos con 0% de descuento
     * 3. ASSERT: Verificamos que el resultado sea 100
     */
    @Test
    public void testCalcularPrecioSinDescuento() {
        // ARRANGE (Preparar)
        double precioBase = 100.0;
        double descuento = 0.0;
        
        // ACT (Actuar)
        double resultado = gestion.calcularPrecioConDescuento(precioBase, descuento);
        
        // ASSERT (Verificar)
        // assertEquals(valorEsperado, valorReal, margenDeError)
        assertEquals(100.0, resultado, 0.01);
        
        System.out.println("✓ Test pasó: Precio sin descuento = " + resultado);
    }
    
    /**
     * TEST 2: Verificar precio con 50% de descuento
     * 
     * ¿Qué estamos probando?
     * - Si el precio es 100 y el descuento es 50%, debe quedar en 50
     * 
     * Cálculo manual: 100 - (100 * 50 / 100) = 100 - 50 = 50
     */
    @Test
    public void testCalcularPrecioCon50PorCientoDescuento() {
        // ARRANGE
        double precioBase = 100.0;
        double descuento = 50.0;
        
        // ACT
        double resultado = gestion.calcularPrecioConDescuento(precioBase, descuento);
        
        // ASSERT
        assertEquals(50.0, resultado, 0.01);
        
        System.out.println("✓ Test pasó: Precio con 50% descuento = " + resultado);
    }
    
    /**
     * TEST 3: Verificar excepción con precio negativo
     * 
     * ¿Qué estamos probando?
     * - El método debe lanzar una IllegalArgumentException si el precio es negativo
     * 
     * @Test(expected = ...) le dice a JUnit:
     * "Espero que este test lance esta excepción específica"
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCalcularPrecioConPrecioNegativo() {
        // ACT & ASSERT
        // Si este método NO lanza la excepción, el test FALLA
        gestion.calcularPrecioConDescuento(-10.0, 20.0);
        
        System.out.println("✓ Test pasó: Se lanzó excepción correctamente");
    }
    
    // ========================================
    // TESTS PARA: estaDisponible()
    // ========================================
    
    /**
     * TEST 4: Libro no disponible (nunca se agregó)
     * 
     * ¿Qué estamos probando?
     * - Si busco un libro que nunca agregué, debe devolver FALSE
     * 
     * Es como buscar "Harry Potter" en una biblioteca vacía → NO está
     */
    @Test
    public void testLibroNoDisponible() {
        // ACT
        // Busco un libro que nunca agregué
        boolean resultado = gestion.estaDisponible("El Señor de los Anillos");
        
        // ASSERT
        // assertFalse verifica que el resultado sea FALSE
        assertFalse(resultado);
        
        System.out.println("✓ Test pasó: Libro no encontrado (como se esperaba)");
    }
    
    /**
     * TEST 5: Libro disponible después de agregarlo
     * 
     * ¿Qué estamos probando?
     * - Si agrego un libro, luego debe estar disponible
     * 
     * Pasos:
     * 1. Agrego "1984" a la biblioteca
     * 2. Pregunto: ¿está disponible "1984"?
     * 3. Debe devolver TRUE
     */
    @Test
    public void testLibroDisponibleDespuesDeAgregar() {
        // ARRANGE
        String libro = "1984";
        
        // ACT
        gestion.agregarLibro(libro);  // Primero lo agrego
        boolean resultado = gestion.estaDisponible(libro);  // Luego pregunto si está
        
        // ASSERT
        // assertTrue verifica que el resultado sea TRUE
        assertTrue(resultado);
        
        System.out.println("✓ Test pasó: Libro encontrado después de agregarlo");
    }
    
    // ========================================
    // TESTS PARA: agregarLibro()
    // ========================================
    
    /**
     * TEST 6: Agregar libro exitosamente
     * 
     * ¿Qué estamos probando?
     * - Que el método agregarLibro() devuelva TRUE cuando se agrega correctamente
     */
    @Test
    public void testAgregarLibroExitosamente() {
        // ACT
        boolean resultado = gestion.agregarLibro("Cien Años de Soledad");
        
        // ASSERT
        // El método debe devolver TRUE si se agregó correctamente
        assertTrue(resultado);
        
        System.out.println("✓ Test pasó: Libro agregado exitosamente");
    }
    
    /**
     * TEST 7: Agregar libro duplicado retorna false
     * 
     * ¿Qué estamos probando?
     * - Si intento agregar el MISMO libro dos veces, la segunda vez debe devolver FALSE
     * 
     * Es como intentar agregar "Don Quijote" dos veces a la biblioteca
     */
    @Test
    public void testAgregarLibroDuplicado() {
        // ARRANGE
        String libro = "Don Quijote";
        
        // ACT
        boolean primerIntento = gestion.agregarLibro(libro);  // Primera vez → TRUE
        boolean segundoIntento = gestion.agregarLibro(libro); // Segunda vez → FALSE
        
        // ASSERT
        assertTrue(primerIntento);   // El primero DEBE ser exitoso
        assertFalse(segundoIntento); // El segundo DEBE fallar (es duplicado)
        
        System.out.println("✓ Test pasó: No se permite duplicados");
    }
    
    // ========================================
    // TESTS PARA: obtenerCategoriaLector()
    // ========================================
    
    /**
     * TEST 8: 0 libros → "Principiante"
     * 
     * ¿Qué estamos probando?
     * - Si alguien ha leído 0 libros, debe ser clasificado como "Principiante"
     */
    @Test
    public void testCategoriaConCeroLibros() {
        // ACT
        String categoria = gestion.obtenerCategoriaLector(0);
        
        // ASSERT
        assertEquals("Principiante", categoria);
        
        System.out.println("✓ Test pasó: 0 libros = Principiante");
    }
    
    /**
     * TEST 9: 5 libros → "Intermedio"
     * 
     * ¿Qué estamos probando?
     * - Si alguien ha leído 5 libros, debe ser "Intermedio"
     * 
     * Según el código: 1-10 libros = "Intermedio"
     */
    @Test
    public void testCategoriaConCincoLibros() {
        // ACT
        String categoria = gestion.obtenerCategoriaLector(5);
        
        // ASSERT
        assertEquals("Intermedio", categoria);
        
        System.out.println("✓ Test pasó: 5 libros = Intermedio");
    }
    
    /**
     * TEST 10: 25 libros → "Avanzado"
     * 
     * ¿Qué estamos probando?
     * - Si alguien ha leído 25 libros, debe ser "Avanzado"
     * 
     * Según el código: 11-50 libros = "Avanzado"
     */
    @Test
    public void testCategoriaConVeinticincoLibros() {
        // ACT
        String categoria = gestion.obtenerCategoriaLector(25);
        
        // ASSERT
        assertEquals("Avanzado", categoria);
        
        System.out.println("✓ Test pasó: 25 libros = Avanzado");
    }
    
    // ========================================
    // TESTS PARA: obtenerLibrosDisponibles()
    // ========================================
    
    /**
     * TEST 11: Nunca retorna null
     * 
     * ¿Qué estamos probando?
     * - El método NUNCA debe devolver null, aunque no haya libros
     * - Debe devolver una lista vacía [], no null
     */
    @Test
    public void testObtenerLibrosNuncaNull() {
        // ACT
        var libros = gestion.obtenerLibrosDisponibles();
        
        // ASSERT
        // assertNotNull verifica que NO sea null
        assertNotNull(libros);
        
        System.out.println("✓ Test pasó: La lista nunca es null");
    }
    
    /**
     * TEST 12: Lista vacía inicialmente
     * 
     * ¿Qué estamos probando?
     * - Cuando creamos una biblioteca nueva, no debe tener libros
     * - La lista debe estar vacía (tamaño = 0)
     */
    @Test
    public void testListaVaciaInicialmente() {
        // ACT
        var libros = gestion.obtenerLibrosDisponibles();
        
        // ASSERT
        assertEquals(0, libros.size());  // El tamaño debe ser 0
        
        System.out.println("✓ Test pasó: La lista está vacía al inicio");
    }
}
