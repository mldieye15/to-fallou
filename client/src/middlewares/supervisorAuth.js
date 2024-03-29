export default function supervisorAuth({ next, router }) {
    if (!localStorage.getItem('token')) {
        return router.push({ name: 'login' });
    }
    
    let role = localStorage.getItem('role');
    if (role !== 'ROLE_SUPERVISSEUR') {
        // Rediriger vers une page non autorisée ou vers une autre page selon vos besoins
        return router.push({ name: 'login' });
    }
    
    return next();
}