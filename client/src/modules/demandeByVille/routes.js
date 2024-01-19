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
            component: () => import( /* webpackChunkName: 'demande_liste' */ './views/Liste.vue')
      },
      {
        path: '/app/dashboard/demandesByVille/:id',
        name: 'demandeByVille-demandes',
        component: () => import( /* webpackChunkName: 'demande_details' */ './views/Demandes.vue'),
      },
      {
        path: '/app/dashboard/demandesByVille/add',
        name: 'demandeByVille-add',
        component: () => import( /* webpackChunkName: 'demande_add' */ './views/Add.vue')
      },
      {
        path: '/app/dashboard/demandesByVille/edit/:id',
        name: 'demandeByVille-edit',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/Edit.vue')
      },
      {
        path: '/app/dashboard/demandes/accepte/:id',
        name: 'accepte-Demande',
        component: () => import( /* webpackChunkName: 'demande_edit */ './views/AccepteDemande.vue')
      }
    ]
}];
export default demandeByVilleRoutes ;