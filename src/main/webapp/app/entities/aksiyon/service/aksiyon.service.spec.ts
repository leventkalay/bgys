import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Onay } from 'app/entities/enumerations/onay.model';
import { IAksiyon, Aksiyon } from '../aksiyon.model';

import { AksiyonService } from './aksiyon.service';

describe('Aksiyon Service', () => {
  let service: AksiyonService;
  let httpMock: HttpTestingController;
  let elemDefault: IAksiyon;
  let expectedResult: IAksiyon | IAksiyon[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AksiyonService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      aciklama: 'AAAAAAA',
      onayDurumu: Onay.ONAYLANMADI,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Aksiyon', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Aksiyon()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Aksiyon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          onayDurumu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Aksiyon', () => {
      const patchObject = Object.assign(
        {
          onayDurumu: 'BBBBBB',
        },
        new Aksiyon()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Aksiyon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          onayDurumu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Aksiyon', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAksiyonToCollectionIfMissing', () => {
      it('should add a Aksiyon to an empty array', () => {
        const aksiyon: IAksiyon = { id: 123 };
        expectedResult = service.addAksiyonToCollectionIfMissing([], aksiyon);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aksiyon);
      });

      it('should not add a Aksiyon to an array that contains it', () => {
        const aksiyon: IAksiyon = { id: 123 };
        const aksiyonCollection: IAksiyon[] = [
          {
            ...aksiyon,
          },
          { id: 456 },
        ];
        expectedResult = service.addAksiyonToCollectionIfMissing(aksiyonCollection, aksiyon);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Aksiyon to an array that doesn't contain it", () => {
        const aksiyon: IAksiyon = { id: 123 };
        const aksiyonCollection: IAksiyon[] = [{ id: 456 }];
        expectedResult = service.addAksiyonToCollectionIfMissing(aksiyonCollection, aksiyon);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aksiyon);
      });

      it('should add only unique Aksiyon to an array', () => {
        const aksiyonArray: IAksiyon[] = [{ id: 123 }, { id: 456 }, { id: 93204 }];
        const aksiyonCollection: IAksiyon[] = [{ id: 123 }];
        expectedResult = service.addAksiyonToCollectionIfMissing(aksiyonCollection, ...aksiyonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aksiyon: IAksiyon = { id: 123 };
        const aksiyon2: IAksiyon = { id: 456 };
        expectedResult = service.addAksiyonToCollectionIfMissing([], aksiyon, aksiyon2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aksiyon);
        expect(expectedResult).toContain(aksiyon2);
      });

      it('should accept null and undefined values', () => {
        const aksiyon: IAksiyon = { id: 123 };
        expectedResult = service.addAksiyonToCollectionIfMissing([], null, aksiyon, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aksiyon);
      });

      it('should return initial array if no Aksiyon is added', () => {
        const aksiyonCollection: IAksiyon[] = [{ id: 123 }];
        expectedResult = service.addAksiyonToCollectionIfMissing(aksiyonCollection, undefined, null);
        expect(expectedResult).toEqual(aksiyonCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
