import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const demandeListe = () => import( /* webpackChunkName: 'demande__liste' */ './views/Liste.vue');
//const demandeDetails = () => import( /* webpackChunkName: 'demande__details' */ './views/Details.vue');

const demandeRoutes = [{
    path: '/app/dashboard/demandes',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'demande-liste',
            component: () => import( /* webpackChunkName: 'demande_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.plannerAuth]
          }
      },
      {
        path: '/app/dashboard/demandes/all',
        name: 'all-demande-liste',
        component: () => import( /* webpackChunkName: 'demande_liste' */ './views/ListeTotalDemande.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
     },
     {
      path: '/app/dashboard/demandes/all/:id',
      name: 'demandeBySession-demandes',
      component: () => import( /* webpackChunkName: 'demande_details' */ './views/DemandesBySession.vue'),
    //   meta: {
    //     middleware: [Middleware.plannerAuth]
    // }
    },
      {
        path: '/app/dashboard/demandes/:id',
        name: 'demande-details',
        component: () => import( /* webpackChunkName: 'demande_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/demandes/add',
        name: 'demande-add',
        component: () => import( /* webpackChunkName: 'demande_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/demandes/edit/:id',
        name: 'demande-edit',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/demandes/accepte/:id',
        name: 'accepte-Demande',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/AccepteDemande.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      }
    ]
}];
export default demandeRoutes;