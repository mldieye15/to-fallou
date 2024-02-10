import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
//const userDetails = () => import( /* webpackChunkName: 'user__details' */ './views/Details.vue');

const adminRoutes = [{
    path: '/app/dashboard/administrateurs',
    component: AppLayout,
    children: [
      {
        path: 'admins',
        name: 'liste-admin',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/ListeAdmin.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: 'planificateurs',
        name: 'liste-planificateur',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/ListePlanificateur.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: 'supervisseurs',
        name: 'liste-supervisseur',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/ListeSupervisseur.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/administrateurs/:id',
        name: 'admin-details',
        component: () => import( /* webpackChunkName: 'user_details' */ './views/Details.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/administrateurs/planificateur-add',
        name: 'planificateur-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/AddPlanif.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/administrateurs/supervisseur-add',
        name: 'supervisseur-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/AddSup.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/administrateurs/add',
        name: 'admin-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/AddAdmin.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/administrateurs/edit/:id',
        name: 'admin-edit',
        component: () => import( /* webpackChunkName: 'user_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      }
    ]
}];
export default adminRoutes;