import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const demandeListe = () => import( /* webpackChunkName: 'demande__liste' */ './views/Liste.vue');
//const demandeDetails = () => import( /* webpackChunkName: 'demande__details' */ './views/Details.vue');

const demandeByVilleRoutes = [{
    path: '/app/dashboard/demandesByVille',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'demandeByVille-liste',
            component: () => import( /* webpackChunkName: 'demande_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.auth]
          }
      },
      {
        path: '/app/dashboard/demandesByVille/:id',
        name: 'demandeByVille-demandes',
        component: () => import( /* webpackChunkName: 'demande_details' */ './views/Demandes.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/demandesByVille/add',
        name: 'demandeByVille-add',
        component: () => import( /* webpackChunkName: 'demande_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/demandesByVille/edit/:id',
        name: 'demandeByVille-edit',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/demandesByVille/accepte/:id',
        name: 'accepte-DemandeByVille',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/AccepteDemande.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      }
    ]
}];
export default demandeByVilleRoutes ;