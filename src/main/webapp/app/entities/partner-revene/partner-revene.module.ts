import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PartnerReveneComponent } from './list/partner-revene.component';
import { PartnerReveneDetailComponent } from './detail/partner-revene-detail.component';
import { PartnerReveneUpdateComponent } from './update/partner-revene-update.component';
import { PartnerReveneDeleteDialogComponent } from './delete/partner-revene-delete-dialog.component';
import { PartnerReveneRoutingModule } from './route/partner-revene-routing.module';

@NgModule({
  imports: [SharedModule, PartnerReveneRoutingModule],
  declarations: [PartnerReveneComponent, PartnerReveneDetailComponent, PartnerReveneUpdateComponent, PartnerReveneDeleteDialogComponent],
})
export class PartnerReveneModule {}
