import { Pipe, PipeTransform } from '@angular/core';

import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatMediumDate',
})
export class FormatMediumDatePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    let res = '';
    if (day) {
      if (typeof day === 'object' && day instanceof dayjs) {
        res = day.format('D MMM YYYY');
      } else {
        const fecha = dayjs(day);
        res = fecha.format('D MMM YYYY');
      }
    }

    return res;
  }
}
