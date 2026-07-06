import { useEffect, useState } from 'react'
import { agregarCliente, extraerMensajeError, generarInventario, obtenerHistorial } from './api/inventarioApi'
import Icon from './components/Icon'

function App() {
  const [clienteId, setClienteId] = useState('')
  const [clienteNombre, setClienteNombre] = useState('')
  const [guardandoCliente, setGuardandoCliente] = useState(false)
  const [mensajeCliente, setMensajeCliente] = useState(null)

  const [generando, setGenerando] = useState(false)
  const [resultado, setResultado] = useState(null)
  const [errorGeneracion, setErrorGeneracion] = useState(null)

  const [historial, setHistorial] = useState([])
  const [cargandoHistorial, setCargandoHistorial] = useState(true)

  const cargarHistorial = async () => {
    try {
      setCargandoHistorial(true)
      const data = await obtenerHistorial()
      setHistorial(data)
    } catch (error) {
      console.error('Error al cargar historial', error)
    } finally {
      setCargandoHistorial(false)
    }
  }

  useEffect(() => {
    cargarHistorial()
  }, [])

  const handleAgregarCliente = async (event) => {
    event.preventDefault()
    setMensajeCliente(null)

    if (!clienteId || !clienteNombre.trim()) {
      setMensajeCliente({ tipo: 'error', texto: 'Debes ingresar ID y nombre del cliente' })
      return
    }

    try {
      setGuardandoCliente(true)
      await agregarCliente({ id: clienteId, nombre: clienteNombre.trim() })
      setMensajeCliente({ tipo: 'exito', texto: 'Cliente agregado correctamente' })
      setClienteId('')
      setClienteNombre('')
    } catch (error) {
      setMensajeCliente({ tipo: 'error', texto: extraerMensajeError(error) })
    } finally {
      setGuardandoCliente(false)
    }
  }

  const handleGenerarInventario = async () => {
    setErrorGeneracion(null)
    try {
      setGenerando(true)
      const data = await generarInventario()
      setResultado(data)
      await cargarHistorial()
    } catch (error) {
      setErrorGeneracion(extraerMensajeError(error))
    } finally {
      setGenerando(false)
    }
  }

  return (
    <div className="page">
      <header className="header">
        <h1>
          <Icon name="warehouse" className="icon-header" />
          Gestión de Inventarios - Operaciones Logísticas
        </h1>
      </header>

      <main className="content">
        <section className="card">
          <h2>
            <Icon name="person_add" />
            Registrar Cliente
          </h2>
          <form className="form-cliente" onSubmit={handleAgregarCliente}>
            <input
              type="number"
              placeholder="ID cliente"
              value={clienteId}
              onChange={(e) => setClienteId(e.target.value)}
            />
            <input
              type="text"
              placeholder="Nombre cliente"
              value={clienteNombre}
              onChange={(e) => setClienteNombre(e.target.value)}
            />
            <button type="submit" disabled={guardandoCliente}>
              <Icon name={guardandoCliente ? 'progress_activity' : 'add_circle'} className={guardandoCliente ? 'icon-spin' : ''} />
              {guardandoCliente ? 'Agregando...' : 'Agregar Cliente'}
            </button>
          </form>
          {mensajeCliente && (
            <p className={`mensaje ${mensajeCliente.tipo}`}>
              <Icon name={mensajeCliente.tipo === 'exito' ? 'check_circle' : 'error'} />
              {mensajeCliente.texto}
            </p>
          )}
        </section>

        <section className="card card-accion">
          <button className="btn-generar" onClick={handleGenerarInventario} disabled={generando}>
            <Icon name={generando ? 'progress_activity' : 'inventory_2'} className={generando ? 'icon-spin' : ''} />
            {generando ? 'Generando...' : 'Generar Inventario de Hoy'}
          </button>
          {errorGeneracion && (
            <p className="mensaje error">
              <Icon name="error" />
              {errorGeneracion}
            </p>
          )}
        </section>

        {resultado && (
          <section className="resultados">
            <div className="resultado-tarjeta ciclico">
              <h3>
                <Icon name="sync" />
                Cliente Cíclico
              </h3>
              <p className="resultado-id">ID: {resultado.clienteCiclico.id}</p>
              <p className="resultado-nombre">{resultado.clienteCiclico.nombre}</p>
            </div>
            <div className="resultado-tarjeta aleatorio">
              <h3>
                <Icon name="shuffle" />
                Cliente Aleatorio - Auditoría
              </h3>
              <p className="resultado-id">ID: {resultado.clienteAleatorio.id}</p>
              <p className="resultado-nombre">{resultado.clienteAleatorio.nombre}</p>
            </div>
          </section>
        )}

        <section className="card">
          <h2>
            <Icon name="history" />
            Historial de Inventarios
          </h2>
          {cargandoHistorial ? (
            <p>
              <Icon name="progress_activity" className="icon-spin" />
              Cargando historial...
            </p>
          ) : historial.length === 0 ? (
            <p>Aún no se han generado inventarios.</p>
          ) : (
            <table className="tabla-historial">
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Cliente Cíclico</th>
                  <th>Cliente Aleatorio</th>
                </tr>
              </thead>
              <tbody>
                {historial.map((item) => (
                  <tr key={item.id}>
                    <td>{new Date(item.fecha).toLocaleString('es-CL')}</td>
                    <td>{item.clienteCiclicoNombre} (ID: {item.clienteCiclicoId})</td>
                    <td>{item.clienteAleatorioNombre} (ID: {item.clienteAleatorioId})</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </section>
      </main>
    </div>
  )
}

export default App