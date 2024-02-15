import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const anneeListe = () => import( /* webpackChunkName: 'annee__liste' */ './views/Liste.vue');
//const anneeDetails = () => import( /* webpackChunkName: 'annee__details' */ './views/Details.vue');

const anneeRoutes = [{
    path: '/app/dashboard/annees',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'annee-liste',
            component: () => import( /* webpackChunkName: 'annee_liste' */ './views/Liste.vue'),
          //   meta: {
          //     middleware: [Middleware.auth]
          // }
      },
      {
        path: '/app/dashboard/annees/:id',
        name: 'annee-details',
        component: () => import( /* webpackChunkName: 'annee_details' */ './views/Details.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/annees/add',
        name: 'annee-add',
        component: () => import( /* webpackChunkName: 'annee_add' */ './views/Add.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/annees/edit/:id',
        name: 'annee-edit',
        component: () => import( /* webpackChunkName: 'annee_edit */ './views/Edit.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      }
    ]
}];
export default anneeRoutes;