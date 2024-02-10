export default function authSup({ next, router }) {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    if (!token || !role) {
        return router.push({ name: 'login' });
    }
    // Vérification du rôle de l'utilisateur
    if (role !== 'ROLE_SUPERVISSEUR') { 
        return router.push({ name: 'access-denied' }); 
    }
    return next();
}