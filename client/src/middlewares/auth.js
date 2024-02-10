export default function auth( { next, router }  ) {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');

    if (!token || !role) {
        return router.push({ name: 'login' });
    }
    // Vérification du rôle de l'utilisateur
    if (role !== 'ROLE_ADMIN' && role !== 'ROLE_PLANIFICATEUR' ){ 
        return router.push({ name: 'access-denied' }); 
    }

    return next();
}