import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const sessionListe = () => import( /* webpackChunkName: 'session__liste' */ './views/Liste.vue');
//const sessionDetails = () => import( /* webpackChunkName: 'session__details' */ './views/Details.vue');

const sessionRoutes = [{
    path: '/app/dashboard/sessions',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'session-liste',
            component: () => import( /* webpackChunkName: 'session_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.auth]
          }
      },
      {
        path: '/app/dashboard/sessions/:id',
        name: 'session-details',
        component: () => import( /* webpackChunkName: 'session_details' */ './views/Details.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/sessions/add',
        name: 'session-add',
        component: () => import( /* webpackChunkName: 'session_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      },
      {
        path: '/app/dashboard/sessions/edit/:id',
        name: 'session-edit',
        component: () => import( /* webpackChunkName: 'session_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.auth]
      }
      }
    ]
}];
export default sessionRoutes;