const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const userListe = () => import( /* webpackChunkName: 'user__liste' */ './views/Liste.vue');
//const userDetails = () => import( /* webpackChunkName: 'user__details' */ './views/Details.vue');

const candidatRoutes = [{
    path: '/app/dashboard/candidats',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'candidat-liste',
            component: () => import( /* webpackChunkName: 'candidat_liste' */ './views/Liste.vue')
      },
      {
        path: '/app/dashboard/candidats/:id',
        name: 'candidat-details',
        component: () => import( /* webpackChunkName: 'candidat_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/candidats/appreciation/:id',
        name: 'candidat-appreciation',
        component: () => import('./views/Appreciation.vue'),
      }
    ]
}];
export default candidatRoutes;