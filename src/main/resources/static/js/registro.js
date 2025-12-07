(function () {
  const form = document.getElementById("registroForm");
  const rolSelect = form.querySelector('select[name="rol"]');
  const campoGrado = document.getElementById("campoGrado");
  const campoEspecialidad = document.getElementById("campoEspecialidad");
  const contrasenaInput = document.getElementById("contrasenaInput");
  const strengthBar = document.getElementById("passwordStrength");

  // Mostrar campos dinámicos según rol
  rolSelect.addEventListener("change", () => {
    campoGrado.style.display = rolSelect.value === "ALUMNO" ? "block" : "none";
    campoEspecialidad.style.display = rolSelect.value === "DOCENTE" ? "block" : "none";
  });


  // Nuevos elementos del sistema Primaria/Secundaria
const nivelSelect = document.getElementById("nivelSelect");
const gradoSelect = document.getElementById("gradoSelect");
const gradoFinal = document.getElementById("gradoFinal");

// Reaccionar cuando se selecciona nivel
if (nivelSelect) {
  nivelSelect.addEventListener("change", () => {
    let grados = [];

    if (nivelSelect.value === "Primaria") {
      grados = ["1ro", "2do", "3ro", "4to", "5to", "6to"];
    } else if (nivelSelect.value === "Secundaria") {
      grados = ["1ro", "2do", "3ro", "4to", "5to"];
    }

    gradoSelect.innerHTML = `<option value="">Seleccione un grado...</option>`;
    grados.forEach(g => {
      const opt = document.createElement("option");
      opt.value = g;
      opt.textContent = g;
      gradoSelect.appendChild(opt);
    });

    gradoFinal.value = ""; // limpiar
  });
}

// Armar resultado final
if (gradoSelect) {
  gradoSelect.addEventListener("change", () => {
    if (nivelSelect.value && gradoSelect.value) {
      gradoFinal.value = `${nivelSelect.value} ${gradoSelect.value}`;
    }
  });
}


  // Patrones de validación
  const patterns = {
    nombre: /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{3,}$/,
    apellido: /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{3,}$/,
    email: /^[a-zA-Z0-9._%+-]+@colegio\.ejemplar$/,
    contrasena: /^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()_+=-]).{8,}$/
  };

  
  function showError(input, message) {
    input.classList.remove("is-valid");
    input.classList.add("is-invalid");
    const msg = input.parentElement.querySelector(".error-msg");
    if (msg) msg.innerHTML = message;
  }

  function showValid(input) {
    input.classList.remove("is-invalid");
    input.classList.add("is-valid");
    const msg = input.parentElement.querySelector(".error-msg");
    if (msg) msg.textContent = "";
  }

  // Evaluar fuerza de la contraseña
  function evaluarFuerza(pass) {
    let fuerza = 0;
    const requisitos = [];

    if (pass.length >= 8) fuerza += 1;
    else requisitos.push("mínimo 8 caracteres");

    if (/[A-Z]/.test(pass)) fuerza += 1;
    else requisitos.push("una mayúscula");

    if (/[a-z]/.test(pass)) fuerza += 1;
    else requisitos.push("una minúscula");

    if (/[0-9]/.test(pass)) fuerza += 1;
    else requisitos.push("un número");

    if (/[!@#$%^&*()_+=-]/.test(pass)) fuerza += 1;
    else requisitos.push("un símbolo");

    return { fuerza, requisitos };
  }

  //Actualizar barra de fuerza
  function actualizarBarra(pass) {
  const msg = contrasenaInput.parentElement.parentElement.querySelector(".error-msg");

  // Si está vacío → limpiar barra y mensaje
  if (pass.trim() === "") {
    strengthBar.innerHTML = "";
    if (msg) msg.textContent = "";
    contrasenaInput.classList.remove("is-valid", "is-invalid");
    return;
  }

  const { fuerza, requisitos } = evaluarFuerza(pass);
  let color, width;

  switch (fuerza) {
    case 5: color = "#198754"; width = "100%"; break; // fuerte
    case 4: color = "#ffc107"; width = "80%"; break;  // buena
    case 3: color = "#fd7e14"; width = "60%"; break;  // media
    default: color = "#dc3545"; width = "40%"; break; // débil
  }

  strengthBar.innerHTML = `
    <div class="progress" style="height:6px;">
      <div class="progress-bar" role="progressbar"
           style="width:${width}; background-color:${color}; transition: width 0.3s ease;"></div>
    </div>
  `;

  if (requisitos.length > 0) {
    msg.innerHTML = "Faltan: " + requisitos.join(", ") + ".";
    contrasenaInput.classList.add("is-invalid");
    contrasenaInput.classList.remove("is-valid");
  } else {
    msg.textContent = "";
    contrasenaInput.classList.add("is-valid");
    contrasenaInput.classList.remove("is-invalid");
  }
}


  // Mostrar/Ocultar contraseña
  const togglePassword = document.getElementById("togglePassword");
  if (togglePassword && contrasenaInput) {
    let visible = false;
    togglePassword.addEventListener("click", () => {
      visible = !visible;
      contrasenaInput.type = visible ? "text" : "password";
      togglePassword.innerHTML = visible
        ? `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
                 class="bi bi-eye-slash" viewBox="0 0 16 16">
             <path d="M13.359 11.238C14.137 10.295 14.776 9.221 15.246 8c-.678-1.705-2.12-3.507-4.1-4.682l.79-.79C14.09 
                      4.3 15.678 6.328 16 8c-.322 1.672-1.91 3.7-4.064 5.472l-.577-.578zM11.704 
                      12.893l-.796.796C9.938 14.32 8.995 14.5 8 
                      14.5c-5 0-8-6.5-8-6.5s1.473-2.748 
                      4.033-4.633L.646 2.354l.708-.708 
                      12 12-.708.708z"/>
           </svg>`
        : `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
                 class="bi bi-eye" viewBox="0 0 16 16">
             <path d="M16 8s-3-5.5-8-5.5S0 8 0 
                      8s3 5.5 8 5.5S16 8 16 8zM8 
                      12a4 4 0 1 1 0-8 4 4 0 0 
                      1 0 8zm0-1.5a2.5 2.5 0 1 
                      0 0-5 2.5 2.5 0 0 0 0 
                      5z"/>
           </svg>`;
    });
  }

  // Validación general
  form.querySelectorAll("input, select").forEach(inp => {
    inp.addEventListener("input", () => {
      if (inp.name === "contrasena") {
        actualizarBarra(inp.value);
      } else {
        validarCampo(inp);
      }
    });
  });

  function validarCampo(input) {
    const name = input.name;
    const val = input.value.trim();

    if (input.hasAttribute("required") && val === "") {
      showError(input, "Campo obligatorio.");
      return false;
    }

    if (patterns[name]) {
      if (!patterns[name].test(val)) {
        if (name === "email") {
          showError(input, "Debe ser un correo institucional: @colegio.ejemplar");
        } else {
          showError(input, "Dato inválido o incompleto.");
        }
        return false;
      }
    }

    showValid(input);
    return true;
  }

  // Validación al enviar
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    let valid = true;

    const inputs = Array.from(form.querySelectorAll("input, select")).filter((inp) => {
      if (inp.name === "grado" && campoGrado.style.display === "none") return false;
      if (inp.name === "especialidad" && campoEspecialidad.style.display === "none") return false;
      return inp.type !== "submit" && inp.type !== "button";
    });

    inputs.forEach((input) => {
      const ok = validarCampo(input);
      if (!ok) valid = false;
    });

    if (rolSelect.value === "ALUMNO") {
      if (!gradoFinal.value.trim()) {
        alert("Selecciona el nivel y el grado del alumno.");
        valid = false;
      }
    }


    if (rolSelect.value === "DOCENTE") {
      const esp = form.querySelector('select[name="especialidad"]');
      if (!esp.value.trim()) {
        showError(esp, "Selecciona la especialidad del docente.");
        valid = false;
      }
    }

    if (valid) form.submit();
  });

  
})();
