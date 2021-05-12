package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  public void init() {
    cuenta = new Cuenta();
  }

  @Test
  public void saldoEsDe1500() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(), 1500, 0);
  }

  @Test
  public void ponerMontoNegativoLanzaExcepcion() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  public void saldoEsDe3856() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(),3856, 0);
  }

  @Test
  public void intentarMasDeTresDepositosLanzaError() {
    assertThrows(MaximaCantidadDepositosException.class, () -> { cuenta.poner(1500); cuenta.poner(456); cuenta.poner(1900); cuenta.poner(245);});
  }

  @Test
  public void extraerMasQueElSaldoLanzaError() {
    assertThrows(SaldoMenorException.class, () -> { cuenta.poner(90); cuenta.sacar(1001);});
  }

  @Test
  public void extraerMasDe1000LanzaError() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> { cuenta.poner(5000); cuenta.sacar(1001);
    });
  }

  @Test
  public void extraerMontoNegativoLanzaError() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void saldoEsDe0() {
    cuenta.poner(1000);
    cuenta.sacar(1000);
    assertEquals(cuenta.getSaldo(), 0, 0);
  }

  @Test
  public void seRegistranDosDepositos() {
    cuenta.poner(5);
    cuenta.poner(7);
    assertEquals(cuenta.getDepositos().size(), 2, 0);
  }

  @Test
  public void seRegistranDosExtracciones() {
    cuenta.poner(100);
    cuenta.sacar(7);
    cuenta.sacar(7);
    assertEquals(cuenta.getExtracciones().size(), 2, 0);
  }

  @Test
  public void cuentaConSaldoInicial() {
    Cuenta cuentaPreexistente = new Cuenta(2000);
    assertEquals(cuentaPreexistente.getSaldo(), 2000, 0);
  }

}