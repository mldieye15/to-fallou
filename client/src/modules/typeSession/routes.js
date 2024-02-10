import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const academieListe = () => import( /* webpackChunkName: 'academie__liste' */ './views/Liste.vue');
//const academieDetails = () => import( /* webpackChunkName: 'academie__details' */ './views/Details.vue');

const typeSessionRoutes = [{
    path: '/app/dashboard/typeSessions',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'typeSession-liste',
            component: () => import( /* webpackChunkName: 'typeSession_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.auth]
          }
      },
      {
        path: '/app/dashboard/typeSessions/:id',
        name: 'typeSession-details',
        component: () => import( /* webpackChunkName: 'typeSession_details' */ './views/Details.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/typeSessions/add',
        name: 'typeSession-add',
        component: () => import( /* webpackChunkName: 'typeSession_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/typeSessions/edit/:id',
        name: 'typeSession-edit',
        component: () => import( /* webpackChunkName: 'typeSession_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      }
    ]
}];
export default typeSessionRoutes;