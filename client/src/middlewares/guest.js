export default function guest( { next, router }  ) {
    if(localStorage.getItem('token')){
        let role= localStorage.getItem('role');
        if(role=='ROLE_USER'){
        return router.push( { name: 'accueil'}) ;
        
        }
        console.log('Redirecting to accueil');
        return router.push( { name: 'dashboard'}) ;
    }
    
    return next();
}