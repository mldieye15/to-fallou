const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const academieListe = () => import( /* webpackChunkName: 'academie__liste' */ './views/Liste.vue');
//const academieDetails = () => import( /* webpackChunkName: 'academie__details' */ './views/Details.vue');

const academieRoutes = [{
    path: '/app/dashboard/academies',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'academie-liste',
            component: () => import( /* webpackChunkName: 'academie_liste' */ './views/Liste.vue')
      },
      {
        path: '/app/dashboard/academies/:id',
        name: 'academie-details',
        component: () => import( /* webpackChunkName: 'academie_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/academies/add',
        name: 'academie-add',
        component: () => import( /* webpackChunkName: 'academie_add' */ './views/Add.vue')
      },
      {
        path: '/app/dashboard/academies/edit/:id',
        name: 'academie-edit',
        component: () => import( /* webpackChunkName: 'academie_edit */ './views/Edit.vue')
      }
    ]
}];
export default academieRoutes;