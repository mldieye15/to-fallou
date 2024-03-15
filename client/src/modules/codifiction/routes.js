import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const codificationListe = () => import( /* webpackChunkName: 'codification__liste' */ './views/Liste.vue');
//const codificationDetails = () => import( /* webpackChunkName: 'academie__details' */ './views/Details.vue');

const codificationRoutes = [{
    path: '/app/dashboard/codifications',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'codification-liste',
            component: () => import( /* webpackChunkName: 'codification_liste' */ './views/Liste.vue'),
          //   meta: {
          //     middleware: [Middleware.auth]
          // }
      },
      {
        path: '/app/dashboard/codifications/:id',
        name: 'codification-details',
        component: () => import( /* webpackChunkName: 'codification_details' */ './views/Details.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/codifications/add',
        name: 'codification-add',
        component: () => import( /* webpackChunkName: 'codification_add' */ './views/Add.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/codifications/edit/:id',
        name: 'codification-edit',
        component: () => import( /* webpackChunkName: 'codification_edit */ './views/Edit.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      }
    ]
}];
export default codificationRoutes;