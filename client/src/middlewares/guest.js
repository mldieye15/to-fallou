export default function guest( { next, router }  ) {
    if(localStorage.getItem('token')){
        let role= localStorage.getItem('username');
        if(role=='test2081'){
        return router.push( { name: 'accueil'}) ;
        
        }
        console.log('Redirecting to accueil');
        return router.push( { name: 'dashboard'}) ;
    }
    
    return next();
}