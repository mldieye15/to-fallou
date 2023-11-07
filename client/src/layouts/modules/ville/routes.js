const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const villeListe = () => import( /* webpackChunkName: 'ville_liste' */ './views/Liste.vue');
//const villeDetails = () => import( /* webpackChunkName: 'ville_details' */ './views/Details.vue');

const academieRoutes = [{
    path: '/app/dashboard/villes',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'ville-liste',
            component: () => import( /* webpackChunkName: 'ville_liste' */ './views/Liste.vue')
      },
      {
        path: '/app/dashboard/villes/:id',
        name: 'ville-details',
        component: () => import( /* webpackChunkName: 'ville_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/villes/add',
        name: 'ville-add',
        component: () => import( /* webpackChunkName: 'ville_add' */ './views/Add.vue')
      },
      {
        path: '/app/dashboard/villes/edit/:id',
        name: 'ville-edit',
        component: () => import( /* webpackChunkName: 'ville_edit */ './views/Edit.vue')
      }
    ]
}];
export default academieRoutes;