package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Cuenta {

  private double saldo = 0;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();

  public Cuenta() {
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public List<Extraccion> getExtracciones() {
    return extracciones;
  }

  public List<Deposito> getDepositos() {
    return depositos;
  }

  public double getSaldo() {
    return saldo;
  }

  double getLimite() {
    return 1000 - getMontoExtraidoA(LocalDate.now());
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getExtraccionesDeUnaFecha(fecha).mapToDouble(extraccion -> extraccion.getMonto()).sum();
  }

  Stream<Extraccion> getExtraccionesDeUnaFecha(LocalDate fecha) {
    return getExtracciones().stream().filter(extraccion -> extraccion.fueExtraido(fecha));
  }

  public void poner(double cuanto) {
    verificarMontoNegativo(cuanto);
    verificarMaximaCantidadDeposito();
    agregarDeposito(LocalDate.now(), cuanto);
    saldo += cuanto;
  }

  public void sacar(double cuanto) {
    verificarMontoNegativo(cuanto);
    verificarSaldoMenor(cuanto);
    verificarExtraccionMaxima(cuanto);
    agregarExtraccion(LocalDate.now(), cuanto);
    saldo -= cuanto;
  }

  public void agregarDeposito(LocalDate fecha, double cuanto) {
    Deposito deposito = new Deposito(fecha, cuanto);
    depositos.add(deposito);
  }

  public void agregarExtraccion(LocalDate fecha, double cuanto) {
    Extraccion extraccion = new Extraccion(fecha, cuanto);
    extracciones.add(extraccion);
  }

  void verificarMontoNegativo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  void verificarSaldoMenor(double cuanto) {
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  void verificarMaximaCantidadDeposito() {
    if (getDepositos().stream().count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  void verificarExtraccionMaxima(double cuanto) {
    if (cuanto > getLimite()) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000 + " diarios, límite: " + getLimite());
    }
  }

}
