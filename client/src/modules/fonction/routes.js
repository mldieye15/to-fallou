const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const fonctionListe = () => import( /* webpackChunkName: 'academie__liste' */ './views/Liste.vue');
//const academieDetails = () => import( /* webpackChunkName: 'academie__details' */ './views/Details.vue');

const fonctionRoutes = [{
    path: '/app/dashboard/fonctions',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'fonction-liste',
            component: () => import( /* webpackChunkName: 'fonction_liste' */ './views/Liste.vue')
      },
      {
        path: '/app/dashboard/fonctions/:id',
        name: 'fonction-details',
        component: () => import( /* webpackChunkName: 'fonction_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/fonctions/add',
        name: 'fonction-add',
        component: () => import( /* webpackChunkName: 'fonction_add' */ './views/Add.vue')
      },
      {
        path: '/app/dashboard/fonctions/edit/:id',
        name: 'fonction-edit',
        component: () => import( /* webpackChunkName: 'fonction_edit */ './views/Edit.vue')
      }
    ]
}];
export default fonctionRoutes;