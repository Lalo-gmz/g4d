import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUser } from 'app/admin/user-management/user-management.model';
import { IParticipacionProyecto } from 'app/entities/participacion-proyecto/participacion-proyecto.model';
import { ParticipacionProyectoService } from 'app/entities/participacion-proyecto/service/participacion-proyecto.service';
import { check } from 'prettier';

@Component({
  selector: 'jhi-participacion',
  templateUrl: './participacion.component.html',
  styleUrls: ['./participacion.component.scss'],
})
export class ParticipacionComponent implements OnInit {
  usuarios?: IUser[];

  proyectoId?: number;
  paricipacion?: IParticipacionProyecto[];
  usuariosEnProyecto: IUser[] = [];

  constructor(protected participacionProyectoService: ParticipacionProyectoService, protected activeRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.proyectoId = this.activeRoute.snapshot.params['id'];
    this.load();
  }

  load(): void {
    if (this.proyectoId)
      this.participacionProyectoService.findByProyecto(this.proyectoId).subscribe({
        next: res => {
          this.paricipacion = res.body ?? [];
          if (res.body) {
            res.body.forEach(e => {
              if (e.usuario) {
                this.usuariosEnProyecto.push(e.usuario);
              }
            });

            this.usuariosEnProyecto.forEach(e => {
              e.check = false;
            });
          }
          console.log('usuarios: ', this.usuariosEnProyecto);
        },
      });

    //obtener todos los
  }

  checked(id: number): void {
    this.usuariosEnProyecto = this.usuariosEnProyecto.map(user => {
      if (user.id === id) {
        return { ...user, check: !user.check };
      }
      return user;
    });
  }

  quitarUsuarios(): void {
    const usersFilter: IUser[] = this.usuariosEnProyecto.filter(user => user.check === true);

    console.log(usersFilter);
  }
}
