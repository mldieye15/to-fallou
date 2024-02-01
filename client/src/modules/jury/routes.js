const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const juryListe = () => import( /* webpackChunkName: 'jury__liste' */ './views/Liste.vue');
//const juryDetails = () => import( /* webpackChunkName: 'jury__details' */ './views/Details.vue');

const juryRoutes = [{
    path: '/app/dashboard/jurys',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'jury-liste',
            component: () => import( /* webpackChunkName: 'jury_liste' */ './views/Liste.vue')
      },
      {
        path: '/app/dashboard/jurys/archives/',
        name: 'jury-liste-archive',
        component: () => import( /* webpackChunkName: 'jury_liste' */ './views/ListeArchive.vue')
  },
      {
        path: '/app/dashboard/jurys/:id',
        name: 'jury-details',
        component: () => import( /* webpackChunkName: 'jury_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/jurys/add',
        name: 'jury-add',
        component: () => import( /* webpackChunkName: 'jury_add' */ './views/Add.vue')
      },
      {
        path: '/app/dashboard/jurys/edit/:id',
        name: 'jury-edit',
        component: () => import( /* webpackChunkName: 'jury_edit */ './views/Edit.vue')
      }
    ]
}];
export default juryRoutes;