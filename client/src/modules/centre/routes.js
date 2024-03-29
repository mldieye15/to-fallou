import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const centreListe = () => import( /* webpackChunkName: 'centre__liste' */ './views/Liste.vue');
//const centreDetails = () => import( /* webpackChunkName: 'centre__details' */ './views/Details.vue');

const centreRoutes = [{
    path: '/app/dashboard/centres',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'centre-liste',
            component: () => import( /* webpackChunkName: 'centre_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.plannerAuth]
          }
      },
      {
        path: '/app/dashboard/centres/:id',
        name: 'centre-details',
        component: () => import( /* webpackChunkName: 'centre_details' */ './views/Details.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/centres/add',
        name: 'centre-add',
        component: () => import( /* webpackChunkName: 'centre_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/centres/edit/:id',
        name: 'centre-edit',
        component: () => import( /* webpackChunkName: 'centre_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      }
    ]
}];
export default centreRoutes;