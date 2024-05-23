import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
// const demandeListe = () => import( /* webpackChunkName: 'demande__liste' */ './views/Liste.vue');
//const demandeDetails = () => import( /* webpackChunkName: 'demande__details' */ './views/Details.vue');

const casRoutes = [{
    path: '/app/dashboard/cas',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'cas-liste',
            component: () => import( /* webpackChunkName: 'cas_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.plannerAuth]
          }
      },
    //   {
    //     path: '/app/dashboard/cass/all',
    //     name: 'all-cas-liste',
    //     component: () => import( /* webpackChunkName: 'cas_liste' */ './views/ListeTotalcas.vue'),
    //     meta: {
    //       middleware: [Middleware.plannerAuth]
    //   }
    //  },
    //  {
    //   path: '/app/dashboard/cass/all/:id',
    //   name: 'casBySession-cass',
    //   component: () => import( /* webpackChunkName: 'cas_details' */ './views/cassBySession.vue'),
    // //   meta: {
    // //     middleware: [Middleware.plannerAuth]
    // // }
    // },
      // {
      //   path: '/app/dashboard/cass/:id',
      //   name: 'cas-details',
      //   component: () => import( /* webpackChunkName: 'cas_details' */ './views/Details.vue'),
      // },
      // {
      //   path: '/app/dashboard/cass/add',
      //   name: 'cas-add',
      //   component: () => import( /* webpackChunkName: 'cas_add' */ './views/Add.vue'),
      //   meta: {
      //     middleware: [Middleware.plannerAuth]
      // }
      // },
      // {
      //   path: '/app/dashboard/cass/edit/:id',
      //   name: 'cas-edit',
      //   component: () => import( /* webpackChunkName: 'cas_edit */ './views/Edit.vue'),
      //   meta: {
      //     middleware: [Middleware.plannerAuth]
      // }
      // },
      // {
      //   path: '/app/dashboard/cass/accept/:id',
      //   name: 'accepte-demande-cas',
      //   component: () => import( /* webpackChunkName: 'cas_edit */ './views/AccepteDemande.vue'),
      //   meta: {
      //     middleware: [Middleware.plannerAuth]
      // }
      // }
    ]
}];
export default casRoutes;