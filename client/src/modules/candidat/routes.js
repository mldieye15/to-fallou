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
        path: '/app/dashboard/candidats/archives',
        name: 'candidat-liste-archive',
        component: () => import( /* webpackChunkName: 'candidat_liste' */ './views/ListeArchive.vue')
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
      },
      {
        path: '/app/dashboard/candidats/bonus/:id',
        name: 'candidat-bonus',
        component: () => import('./views/Bonus.vue'),
      },
      {
        path: '/app/dashboard/candidats/malus/:id',
        name: 'candidat-malus',
        component: () => import('./views/Malus.vue'),
      }
    ]
}];
export default candidatRoutes;