import axios from 'axios'

// window.__ENV__ es inyectado en runtime por el contenedor (ver docker-entrypoint.sh),
// lo que permite usar la misma imagen Docker en distintos ambientes sin reconstruirla.
const API_URL = window.__ENV__?.API_URL || import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export async function agregarCliente({ id, nombre }) {
  const { data } = await apiClient.post('/clientes', { id: Number(id), nombre })
  return data
}

export async function generarInventario() {
  const { data } = await apiClient.post('/inventarios/generar')
  return data
}

export async function obtenerHistorial() {
  const { data } = await apiClient.get('/inventarios')
  return data
}

export function extraerMensajeError(error) {
  return error?.response?.data?.message || error?.message || 'Ocurrió un error inesperado'
}