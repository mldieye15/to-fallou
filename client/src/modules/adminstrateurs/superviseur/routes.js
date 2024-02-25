import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
//const userDetails = () => import( /* webpackChunkName: 'user__details' */ './views/Details.vue');

const supRoutes = [{
    path: '/app/dashboard/superviseurs',
    component: AppLayout,
    children: [
      {
        path: 'superviseurs',
        name: 'sup-liste',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/Liste.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/superviseurs/:id',
        name: 'sup-details',
        component: () => import( /* webpackChunkName: 'user_details' */ './views/Details.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/superviseurs/add',
        name: 'sup-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/Add.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/administrateurs/edit/:id',
        name: 'sup-edit',
        component: () => import( /* webpackChunkName: 'user_edit */ './views/Edit.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      }
    ]
}];
export default supRoutes;