const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
//const userDetails = () => import( /* webpackChunkName: 'user__details' */ './views/Details.vue');

const adminRoutes = [{
    path: '/app/dashboard/administrateurs',
    component: AppLayout,
    children: [
      {
        path: 'admins',
        name: 'liste-admin',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/ListeAdmin.vue')
      },
      {
        path: 'planificateurs',
        name: 'liste-planificateur',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/ListePlanificateur.vue')
      },
      {
        path: 'supervisseurs',
        name: 'liste-supervisseur',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/ListeSupervisseur.vue')
      },
      {
        path: '/app/dashboard/administrateurs/:id',
        name: 'admin-details',
        component: () => import( /* webpackChunkName: 'user_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/administrateurs/planificateur-add',
        name: 'planificateur-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/AddPlanif.vue')
      },
      {
        path: '/app/dashboard/administrateurs/supervisseur-add',
        name: 'supervisseur-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/AddSup.vue')
      },
      {
        path: '/app/dashboard/administrateurs/add',
        name: 'admin-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/AddAdmin.vue')
      },
      {
        path: '/app/dashboard/administrateurs/edit/:id',
        name: 'admin-edit',
        component: () => import( /* webpackChunkName: 'user_edit */ './views/Edit.vue')
      }
    ]
}];
export default adminRoutes;