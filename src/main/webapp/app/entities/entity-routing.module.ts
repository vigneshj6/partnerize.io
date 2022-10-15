import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'partner',
        data: { pageTitle: 'partnerizeApp.partner.home.title' },
        loadChildren: () => import('./partner/partner.module').then(m => m.PartnerModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'partnerizeApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'currency',
        data: { pageTitle: 'partnerizeApp.currency.home.title' },
        loadChildren: () => import('./currency/currency.module').then(m => m.CurrencyModule),
      },
      {
        path: 'card',
        data: { pageTitle: 'partnerizeApp.card.home.title' },
        loadChildren: () => import('./card/card.module').then(m => m.CardModule),
      },
      {
        path: 'contract',
        data: { pageTitle: 'partnerizeApp.contract.home.title' },
        loadChildren: () => import('./contract/contract.module').then(m => m.ContractModule),
      },
      {
        path: 'sale',
        data: { pageTitle: 'partnerizeApp.sale.home.title' },
        loadChildren: () => import('./sale/sale.module').then(m => m.SaleModule),
      },
      {
        path: 'partner-payment',
        data: { pageTitle: 'partnerizeApp.partnerPayment.home.title' },
        loadChildren: () => import('./partner-payment/partner-payment.module').then(m => m.PartnerPaymentModule),
      },
      {
        path: 'payment-event',
        data: { pageTitle: 'partnerizeApp.paymentEvent.home.title' },
        loadChildren: () => import('./payment-event/payment-event.module').then(m => m.PaymentEventModule),
      },

      {
        path: 'customer',
        data: { pageTitle: 'partnerizeApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
