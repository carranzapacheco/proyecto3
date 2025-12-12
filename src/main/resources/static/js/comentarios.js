(() => {
  // Detectar entorno: localhost o producción
  const API_URL = window.location.hostname.includes("localhost")
    ? "http://localhost:8080/api/comentario"
    : "/api/comentario";

  const $lista = document.getElementById("lista-comentarios");
  const $form = document.getElementById("form-comentario");
  const $nombre = document.getElementById("nombre-comentario");
  const $comentario = document.getElementById("comentario");

  if (!$lista || !$form) return;

  // Escapar texto para evitar XSS
  const esc = (s) =>
    (s || "").replace(/[&<>"']/g, (m) => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[m]));

  const updateCount = (n) => {
    const badge = document.getElementById("contador-comentarios");
    if (badge) badge.textContent = `${n} comentario${n === 1 ? "" : "s"}`;
  };

  // Cargar comentarios desde la API
  async function cargar() {
    try {
      const res = await fetch(API_URL);
      const datos = await res.json();

      if (!Array.isArray(datos) || !datos.length) {
        $lista.innerHTML = '<div class="p-3 text-center text-muted">Aún no hay comentarios. ¡Sé el primero en opinar!</div>';
        updateCount(0);
        return;
      }

      $lista.innerHTML = datos.reverse().map((c) => `
        <div class="comment-item d-flex align-items-start p-3 border-bottom" data-id="${c.id}">
          <div class="comment-avatar me-3">${esc((c.nombre || "A").charAt(0).toUpperCase())}</div>
          <div class="comment-body flex-grow-1">
            <div class="d-flex justify-content-between align-items-start">
              <h6 class="mb-1">${esc(c.nombre || "Anónimo")}</h6>
              <div class="d-flex align-items-center gap-2">
                <small class="comment-meta">${new Date(c.fecha).toLocaleString("es-PE")}</small>
              </div>
            </div>
            <p class="mb-0">${esc(c.texto)}</p>
          </div>
        </div>
      `).join("");

      updateCount(datos.length);
    } catch (err) {
      console.error("Error al cargar comentarios:", err);
      $lista.innerHTML = '<div class="p-3 text-center text-danger">Error al cargar los comentarios.</div>';
    }
  }

  // Enviar nuevo comentario con validación visual
  $form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Limpiar estados anteriores
    $nombre.classList.remove("is-invalid");
    $comentario.classList.remove("is-invalid");
    document.getElementById("error-nombre").textContent = "";
    document.getElementById("error-comentario").textContent = "";

    const nombre = $nombre.value.trim();
    const texto = $comentario.value.trim();

    try {
      const res = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre, texto }),
      });

      const data = await res.json();

      // Si hay errores de validación
      if (Array.isArray(data)) {
        data.forEach((msg) => {
          if (msg.toLowerCase().includes("nombre")) {
            $nombre.classList.add("is-invalid");
            document.getElementById("error-nombre").textContent = msg;
          }
          if (msg.toLowerCase().includes("mensaje") || msg.toLowerCase().includes("texto")) {
            $comentario.classList.add("is-invalid");
            document.getElementById("error-comentario").textContent = msg;
          }
        });
        return;
      }

      // Si todo está bien
      $comentario.value = "";
      $nombre.value = "";
      $form.classList.remove("was-validated");
      cargar();

      // Mostrar Modal
      const modal = new bootstrap.Modal(document.getElementById("modalPublicado"));
      modal.show();

    } catch (err) {
      console.error("Error al enviar comentario:", err);
      alert("No se pudo enviar el comentario. Inténtalo más tarde.");
    }
  });

  cargar();

})();
