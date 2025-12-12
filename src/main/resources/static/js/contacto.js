(() => {
  const API_URL = "https://colegio-cekh.onrender.com/api/contacto";
  const $form = document.querySelector(".container form");
  const modal = new bootstrap.Modal(document.getElementById("modalContacto"));
  const modalTitulo = document.getElementById("modalTitulo");
  const modalMensaje = document.getElementById("modalMensaje");

  if (!$form) return;

  const campos = ["nombre", "email", "asunto", "mensaje"];
  const inputs = Object.fromEntries(campos.map(id => [id, document.getElementById(id)]));

  // Mostrar error
  const mostrarError = (campo, mensaje) => {
    const input = inputs[campo];
    input.classList.remove("is-valid");
    input.classList.add("is-invalid");
    let feedback = input.parentElement.querySelector(".invalid-feedback");
    if (!feedback) {
      feedback = document.createElement("div");
      feedback.classList.add("invalid-feedback");
      input.parentElement.appendChild(feedback);
    }
    feedback.textContent = mensaje;
  };

  // Mostrar válido
  const marcarValido = (campo) => {
    const input = inputs[campo];
    input.classList.remove("is-invalid");
    input.classList.add("is-valid");
    const fb = input.parentElement.querySelector(".invalid-feedback");
    if (fb) fb.remove();
  };

  // Limpiar errores previos
  const limpiarErrores = () => {
    campos.forEach(c => {
      inputs[c].classList.remove("is-invalid", "is-valid");
      const fb = inputs[c].parentElement.querySelector(".invalid-feedback");
      if (fb) fb.remove();
    });
  };

  // Validación dinámica al escribir (para feedback instantáneo)
  campos.forEach(campo => {
    inputs[campo].addEventListener("input", () => {
      validarCampo(campo, inputs[campo].value.trim());
    });
  });

  const validarCampo = (campo, valor) => {
    switch (campo) {
      case "nombre":
        if (valor.length < 2) return mostrarError(campo, "El nombre debe tener mínimo 2 caracteres.");
        break;
      case "email":
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor))
          return mostrarError(campo, "Ingresa un correo electrónico válido.");
        break;
      case "asunto":
        if (valor.length < 3) return mostrarError(campo, "El asunto debe tener al menos 3 caracteres.");
        break;
      case "mensaje":
        if (valor.length < 10) return mostrarError(campo, "El mensaje debe tener al menos 10 caracteres.");
        break;
    }
    marcarValido(campo);
  };

  $form.addEventListener("submit", async (e) => {
    e.preventDefault();
    limpiarErrores();

    const datos = Object.fromEntries(campos.map(c => [c, inputs[c].value.trim()]));
    let valido = true;

    // Validaciones frontend antes de enviar
    campos.forEach(c => {
      const valor = datos[c];
      const antes = inputs[c].classList.contains("is-invalid");
      validarCampo(c, valor);
      if (inputs[c].classList.contains("is-invalid")) valido = false;
      // Si ya tenía error y no lo corrigió, sigue inválido
    });

    if (!valido) return;

    try {
      const res = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datos),
      });

      const data = await res.json();

      if (Array.isArray(data)) {
        // Errores del backend
        data.forEach(msg => {
          if (msg.toLowerCase().includes("nombre")) mostrarError("nombre", msg);
          if (msg.toLowerCase().includes("correo") || msg.toLowerCase().includes("email")) mostrarError("email", msg);
          if (msg.toLowerCase().includes("asunto")) mostrarError("asunto", msg);
          if (msg.toLowerCase().includes("mensaje")) mostrarError("mensaje", msg);
        });
        return;
      }

      // Éxito
      $form.reset();
      limpiarErrores();
      modalTitulo.textContent = "✅ Mensaje enviado correctamente";
      modalMensaje.textContent = "Gracias por contactarnos. Te responderemos pronto.";
      modal.show();
    } catch (error) {
      console.error("Error al enviar contacto:", error);
      modalTitulo.textContent = "⚠️ Error al enviar el mensaje";
      modalMensaje.textContent = "No se pudo enviar tu mensaje. Inténtalo más tarde.";
      modal.show();
    }
  });
})();
