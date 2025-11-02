package sebas.lab.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas unitarias para UserService usando Mockito
 * 
 * Mockito permite crear "mocks" (objetos simulados) para probar
 * clases que dependen de otras sin necesitar implementaciones reales.
 */
public class TestUserService {
    
    // El mock del repositorio (objeto simulado)
    private UserRepository mockRepository;
    
    // El servicio que vamos a probar (usa el mock)
    private UserService userService;
    
    /**
     * Este método se ejecuta ANTES de cada test
     * 
     * Aquí configuramos:
     * 1. Creamos un mock (simulación) de UserRepository
     * 2. Creamos una instancia real de UserService
     * 3. Le inyectamos el mock al servicio
     */
    @Before
    public void setUp() {
        // Paso 1: Crear el mock usando Mockito.mock()
        // Esto crea un objeto "falso" de UserRepository
        mockRepository = mock(UserRepository.class);
        
        // Paso 2: Crear el servicio real e inyectarle el mock
        // El servicio cree que mockRepository es un repositorio real
        userService = new UserService(mockRepository);
    }
    
    // ========================================
    // TEST 1: Usuario Existe (Happy Path)
    // ========================================
    
    /**
     * TEST: Obtener nombre de usuario que existe
     * 
     * Escenario:
     * - Existe un usuario con ID "123" y nombre "Juan"
     * - Llamamos a getUserName("123")
     * - Debe retornar "Juan"
     * 
     * Pasos:
     * 1. ARRANGE: Configuramos el mock para que devuelva un usuario
     * 2. ACT: Llamamos al método getUserName()
     * 3. ASSERT: Verificamos que retorne el nombre correcto
     * 4. VERIFY: Verificamos que se llamó al repositorio exactamente 1 vez
     */
    @Test
    public void testGetUserNameCuandoUsuarioExiste() {
        // ARRANGE (Preparar)
        // Creamos un usuario de prueba
        User usuarioMock = new User("123", "Juan");
        
        // Configuramos el comportamiento del mock:
        // "Cuando alguien llame a findById con '123', devuelve usuarioMock"
        when(mockRepository.findById("123")).thenReturn(usuarioMock);
        
        // ACT (Actuar)
        // Llamamos al método que queremos probar
        String resultado = userService.getUserName("123");
        
        // ASSERT (Verificar el resultado)
        // Verificamos que el nombre retornado sea "Juan"
        assertEquals("Juan", resultado);
        
        // VERIFY (Verificar interacciones)
        // Verificamos que el método findById fue llamado exactamente 1 vez con "123"
        // Esto asegura que el servicio SI usó el repositorio correctamente
        verify(mockRepository, times(1)).findById("123");
        
        System.out.println("Test pasado: Usuario encontrado correctamente");
    }
    
    // ========================================
    // TEST 2: Usuario No Existe (Error)
    // ========================================
    
    /**
     * TEST: Obtener nombre de usuario que NO existe
     * 
     * Escenario:
     * - NO existe un usuario con ID "999"
     * - El repositorio devuelve null
     * - getUserName("999") debe lanzar IllegalArgumentException
     * - El mensaje debe ser "User not found"
     * 
     * Pasos:
     * 1. ARRANGE: Configuramos el mock para que devuelva null
     * 2. ACT: Llamamos al método getUserName() dentro de un try-catch
     * 3. ASSERT: Verificamos que se lanzó la excepción correcta
     * 4. ASSERT: Verificamos que el mensaje sea el correcto
     * 5. VERIFY: Verificamos que se llamó al repositorio
     */
    @Test
    public void testGetUserNameCuandoUsuarioNoExiste() {
        // ARRANGE (Preparar)
        // Configuramos el mock para que devuelva null cuando busquemos "999"
        // Esto simula que el usuario no existe en la base de datos
        when(mockRepository.findById("999")).thenReturn(null);
        
        // ACT & ASSERT (Actuar y Verificar)
        try {
            // Intentamos obtener el nombre de un usuario que no existe
            userService.getUserName("999");
            
            // Si llegamos aquí, el test FALLA porque NO se lanzó la excepción
            fail("Se esperaba una IllegalArgumentException pero no se lanzó");
            
        } catch (IllegalArgumentException e) {
            // ASSERT: Verificamos que el mensaje de error sea el correcto
            assertEquals("User not found", e.getMessage());
            
            System.out.println("Test pasado: Excepción lanzada correctamente");
        }
        
        // VERIFY (Verificar interacciones)
        // Verificamos que el repositorio fue consultado 1 vez
        verify(mockRepository, times(1)).findById("999");
    }
    
    // ========================================
    // TESTS ADICIONALES (Opcional)
    // ========================================
    
    /**
     * TEST ADICIONAL: Verificar que no se puede crear UserService sin repositorio
     * 
     * Este test verifica que el constructor requiere un repositorio válido
     */
    @Test(expected = NullPointerException.class)
    public void testUserServiceRequiereRepositorio() {
        // Si intentamos crear un UserService con null, debe fallar
        UserService servicioInvalido = new UserService(null);
        
        // Intentamos usar el servicio (esto debe lanzar NullPointerException)
        servicioInvalido.getUserName("123");
    }
    
    /**
     * TEST ADICIONAL: Verificar comportamiento con ID null
     * 
     * Este test verifica qué pasa si buscamos un usuario con ID null
     */
    @Test
    public void testGetUserNameConIdNull() {
        // ARRANGE
        // Configuramos el mock para que devuelva null cuando el ID sea null
        when(mockRepository.findById(null)).thenReturn(null);
        
        // ACT & ASSERT
        try {
            userService.getUserName(null);
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("User not found", e.getMessage());
        }
        
        // VERIFY
        verify(mockRepository, times(1)).findById(null);
    }
    
    /**
     * TEST ADICIONAL: Verificar que el mock nunca llama a métodos no configurados
     * 
     * Este test demuestra que si no configuramos un comportamiento,
     * el mock devuelve valores por defecto (null para objetos)
     */
    @Test
    public void testMockDevuelveNullPorDefecto() {
        // No configuramos ningún comportamiento con when()
        
        // ACT
        User resultado = mockRepository.findById("cualquierID");
        
        // ASSERT
        // Por defecto, un mock devuelve null para métodos que retornan objetos
        assertNull(resultado);
        
        System.out.println("Test pasado: Mock devuelve null sin configuración");
    }
}
