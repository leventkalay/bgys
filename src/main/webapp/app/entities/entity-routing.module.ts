import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'varlik',
        data: { pageTitle: 'bgysApp.varlik.home.title' },
        loadChildren: () => import('./varlik/varlik.module').then(m => m.VarlikModule),
      },
      {
        path: 'aksiyon',
        data: { pageTitle: 'bgysApp.aksiyon.home.title' },
        loadChildren: () => import('./aksiyon/aksiyon.module').then(m => m.AksiyonModule),
      },
      {
        path: 'tehdit',
        data: { pageTitle: 'bgysApp.tehdit.home.title' },
        loadChildren: () => import('./tehdit/tehdit.module').then(m => m.TehditModule),
      },
      {
        path: 'risk',
        data: { pageTitle: 'bgysApp.risk.home.title' },
        loadChildren: () => import('./risk/risk.module').then(m => m.RiskModule),
      },
      {
        path: 'varlik-kategorisi',
        data: { pageTitle: 'bgysApp.varlikKategorisi.home.title' },
        loadChildren: () => import('./varlik-kategorisi/varlik-kategorisi.module').then(m => m.VarlikKategorisiModule),
      },
      {
        path: 'tehdit-kategorisi',
        data: { pageTitle: 'bgysApp.tehditKategorisi.home.title' },
        loadChildren: () => import('./tehdit-kategorisi/tehdit-kategorisi.module').then(m => m.TehditKategorisiModule),
      },
      {
        path: 'surec',
        data: { pageTitle: 'bgysApp.surec.home.title' },
        loadChildren: () => import('./surec/surec.module').then(m => m.SurecModule),
      },
      {
        path: 'is-akisi',
        data: { pageTitle: 'bgysApp.isAkisi.home.title' },
        loadChildren: () => import('./is-akisi/is-akisi.module').then(m => m.IsAkisiModule),
      },
      {
        path: 'personel',
        data: { pageTitle: 'bgysApp.personel.home.title' },
        loadChildren: () => import('./personel/personel.module').then(m => m.PersonelModule),
      },
      {
        path: 'birim',
        data: { pageTitle: 'bgysApp.birim.home.title' },
        loadChildren: () => import('./birim/birim.module').then(m => m.BirimModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
