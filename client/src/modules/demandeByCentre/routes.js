import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const demandeListe = () => import( /* webpackChunkName: 'demande__liste' */ './views/Liste.vue');
//const demandeDetails = () => import( /* webpackChunkName: 'demande__details' */ './views/Details.vue');

const demandeByCentreRoutes = [{
    path: '/app/dashboard/demandesByCentre',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'demandeByCentre-liste',
            component: () => import( /* webpackChunkName: 'demande_liste' */ './views/Liste.vue'),
          //   meta: {
          //     middleware: [Middleware.auth]
          // }
      },
      {
        path: '/app/dashboard/demandesByCentre/:id',
        name: 'demandeByCentre-demandes',
        component: () => import( /* webpackChunkName: 'demande_details' */ './views/Demandes.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/demandesByCentre/add',
        name: 'demandeByCentre-add',
        component: () => import( /* webpackChunkName: 'demande_add' */ './views/Add.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/demandesByCentre/edit/:id',
        name: 'demandeByCentre-edit',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/Edit.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/demandesByCentre/affecter/:id',
        name: 'affecter-DemandeByCentre',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/Affecter.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      }
    ]
}];
export default demandeByCentreRoutes ;