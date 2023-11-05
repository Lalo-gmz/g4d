import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUser } from 'app/entities/user/user.model';
import { IParticipacionProyecto, NewParticipacionProyecto } from 'app/entities/participacion-proyecto/participacion-proyecto.model';
import { ParticipacionProyectoService } from 'app/entities/participacion-proyecto/service/participacion-proyecto.service';
import { UserService } from 'app/entities/user/user.service';
import { IProyecto } from '../proyecto.model';
import { ProyectoService } from '../service/proyecto.service';

@Component({
  selector: 'jhi-participacion',
  templateUrl: './participacion.component.html',
})
export class ParticipacionComponent implements OnInit {
  usuarios: IUser[] = [];

  proyectoId?: number;
  proyecto?: IProyecto;
  paricipacion: IParticipacionProyecto[] = [];
  usuariosEnProyecto: IUser[] = [];

  constructor(
    protected participacionProyectoService: ParticipacionProyectoService,
    protected activeRoute: ActivatedRoute,
    protected userService: UserService,
    protected proyectoService: ProyectoService
  ) {}

  ngOnInit(): void {
    this.proyectoId = this.activeRoute.snapshot.params['id'];
    if (this.proyectoId)
      this.proyectoService.find(this.proyectoId).subscribe({
        next: res => {
          if (res.body) this.proyecto = res.body;
        },
      });
    this.load();
  }

  load(): void {
    if (this.proyectoId)
      this.participacionProyectoService.findByProyecto(this.proyectoId).subscribe({
        next: (res): void => {
          this.paricipacion = res.body ?? [];

          if (res.body) {
            this.paricipacion.forEach(e => {
              e.check = false;
              if (e.usuario?.check) e.usuario.check = true;
            });
          }

          if (res.body) {
            this.usuariosEnProyecto = [];
            res.body.forEach(e => {
              if (e.usuario) {
                this.usuariosEnProyecto.push(e.usuario);
              }
            });

            this.usuariosEnProyecto.forEach(e => {
              e.check = false;
            });
          }

          this.userService.query().subscribe({
            next: res => {
              this.usuarios = res.body ?? [];

              this.usuarios.forEach(e => {
                e.check = false;
              });

              this.usuarios = this.usuarios.filter(obj => !this.usuariosEnProyecto.map(o => o.id).includes(obj.id));
            },
          });
        },
      });
  }

  checked(id: number | undefined): void {
    if (typeof id !== undefined) {
      this.usuariosEnProyecto = this.usuariosEnProyecto.map(user => {
        if (user.id === id) {
          return { ...user, check: !user.check };
        }
        return user;
      });

      this.paricipacion = this.paricipacion.map(parti => {
        if (parti.id === id) {
          return { ...parti, check: !parti.check };
        }
        return parti;
      });
    }
  }

  checked2(id: number): void {
    this.usuarios = this.usuarios.map(user => {
      if (user.id === id) {
        return { ...user, check: !user.check };
      }
      return user;
    });
  }

  quitarUsuarios(): void {
    const usersFilter: IParticipacionProyecto[] = this.paricipacion.filter(user => user.check === true);

    this.participacionProyectoService.deleteAll(usersFilter).subscribe({
      next: () => {
        this.load();
      },
    });
  }

  agregarUsuarios(): void {
    const usersFilter: IUser[] = this.usuarios.filter(user => user.check === true);

    const participantes: NewParticipacionProyecto[] = [];
    usersFilter.forEach(e => {
      participantes.push({ id: null, esAdmin: false, proyecto: this.proyecto, usuario: e });
    });

    this.participacionProyectoService.createAll(participantes).subscribe({
      next: () => {
        this.load();
      },
    });
  }

  previousState(): void {
    window.history.back();
  }
}
