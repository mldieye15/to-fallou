const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const etablissementListe = () => import( /* webpackChunkName: 'etablissement__liste' */ './views/Liste.vue');
//const etablissementDetails = () => import( /* webpackChunkName: 'etablissement__details' */ './views/Details.vue');

const etablissementRoutes = [{
    path: '/app/dashboard/etablissements',
    component: AppLayout,
    children: [
      {
            path: '', 
            name: 'etablissement-liste',
            component: () => import( /* webpackChunkName: 'etablissement_liste' */ './views/Liste.vue')
      },
      {
        path: '/app/dashboard/etablissements/:id',
        name: 'etablissement-details',
        component: () => import( /* webpackChunkName: 'etablissement_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/etablissements/add',
        name: 'etablissement-add',
        component: () => import( /* webpackChunkName: 'etablissement_add' */ './views/Add.vue')
      },
      {
        path: '/app/dashboard/etablissements/edit/:id',
        name: 'etablissement-edit',
        component: () => import( /* webpackChunkName: 'etablissement_edit */ './views/Edit.vue')
      }
    ]
}];
export default etablissementRoutes;