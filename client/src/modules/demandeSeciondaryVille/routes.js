import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const demandeListe = () => import( /* webpackChunkName: 'demande__liste' */ './views/Liste.vue');
//const demandeDetails = () => import( /* webpackChunkName: 'demande__details' */ './views/Details.vue');

const demandeSecondaryVilleRoutes = [{
    path: '/app/dashboard/demandesSecondaryVille',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'demandeSecondaryVille-liste',
            component: () => import( /* webpackChunkName: 'demande_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.plannerAuth]
          }
      },
      {
        path: '/app/dashboard/demandesSecondaryVille/:id',
        name: 'demandeSecondaryVille-demandes',
        component: () => import( /* webpackChunkName: 'demande_details' */ './views/Demandes.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/demandesSecondaryVille/add',
        name: 'demandeSecondaryVille-add',
        component: () => import( /* webpackChunkName: 'demande_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/demandesSecondaryVille/edit/:id',
        name: 'demandeSecondaryVille-edit',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/demandesSecondaryVille/accepte/:id',
        name: 'accepte-DemandeSecondaryVille',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/AccepteDemande.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      }
    ]
}];
export default demandeSecondaryVilleRoutes ;