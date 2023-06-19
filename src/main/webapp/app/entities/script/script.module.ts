import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ScriptComponent } from './list/script.component';
import { ScriptDeleteDialogComponent } from './delete/script-delete-dialog.component';
import { ScriptRoutingModule } from './route/script-routing.module';
import { ScriptDetailComponent } from './detail/script-detail.component';
import { ScriptUpdateComponent } from './update/script-update.component';

@NgModule({
  imports: [SharedModule, ScriptRoutingModule],
  declarations: [ScriptComponent, ScriptDeleteDialogComponent, ScriptDetailComponent, ScriptUpdateComponent, ScriptDetailComponent],
})
export class ScriptModule {}
