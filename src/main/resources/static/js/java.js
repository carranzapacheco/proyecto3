// Modal de inicio (solo se muestra una vez por sesión)
  document.addEventListener('DOMContentLoaded', function () {
    // Revisar si ya se mostró el modal en esta sesión
    if (!sessionStorage.getItem('modalMostrado')) {
      var inicioModal = new bootstrap.Modal(document.getElementById('inicioModal'));
      inicioModal.show();
      // Guardar en sessionStorage que ya se mostró
      sessionStorage.setItem('modalMostrado', 'true');
    }
  });

  
  // Ocultar skeleton cuando carga el iframe
  function hideSkeleton(){ const s=document.getElementById('mapSkeleton'); if(s) s.style.display='none'; }

  // Reveal on scroll
  const obs=new IntersectionObserver((entries)=>{
    entries.forEach(e=>{ if(e.isIntersecting){ e.target.classList.add('revealed'); obs.unobserve(e.target);} });
  },{threshold:.12});
  document.querySelectorAll('.reveal').forEach(el=>obs.observe(el));

  // Contadores (KPIs)
  const easeOut=(t)=>1-Math.pow(1-t,3);
  const animateCount=(el)=>{
    const target=+el.dataset.count; const dur=900; let st=null;
    const step=(ts)=>{ if(!st) st=ts; const p=Math.min(1,(ts-st)/dur); el.textContent=Math.floor(easeOut(p)*target).toLocaleString(); if(p<1) requestAnimationFrame(step); else el.textContent=target.toLocaleString(); };
    requestAnimationFrame(step);
  };
  document.querySelectorAll('.kpi-num').forEach(animateCount);
